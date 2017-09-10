import java.util.concurrent.CountDownLatch;

public class Runner {

    private final Class<? extends BaseWorker> clazz;
    private final int loops;
    private final int threads;
    private final BaseWorker[] workers;
    private final CountDownLatch latch;

    public Runner(Class<? extends BaseWorker> clazz, int loops, int threads)
            throws IllegalAccessException, InstantiationException {
        this.clazz = clazz;
        this.loops = loops;
        this.threads = threads;
        this.latch = new CountDownLatch(threads);
        this.workers = new BaseWorker[threads];
        for (int i = 0; i < threads; i++) {
            if (clazz == ITextWorker.class) {
                workers[i] = new ITextWorker(loops, latch);
            } else if (clazz == FlyingSaucerWorker.class) {
                workers[i] = new FlyingSaucerWorker(loops, latch);
            } else {
                throw new IllegalArgumentException("Not supported worker:" + clazz);
            }
        }
    }

    public void run() throws InterruptedException {
        System.out.println();
        System.out.println(this.clazz.getSimpleName());
        System.out.println("Run " + loops + " times with " + threads + " threads");
        long t1 = System.currentTimeMillis();
        for (BaseWorker worker : workers) {
            worker.run();
        }
        latch.await();
        long duration = System.currentTimeMillis() - t1;
        System.out.println();
        System.out.println("Duration(ms):" + duration);
        System.out.println("TPS:" + ((double) loops / duration * 1000));
        System.out.println("Average Latency:" + ((double) duration / loops));

        System.gc();
        Thread.sleep(2000);
    }

    public static void main(String[] args) throws Exception {
        // warm up
        System.out.println("================== Warm up ===========================");
        new Runner(ITextWorker.class, 1, 1).run();
        new Runner(FlyingSaucerWorker.class, 1, 1).run();

        for (int threads = 1; threads < 32; threads *= 2) {
            // start
            System.out.println("================== Test with " + threads + "threads ==================");
            new Runner(ITextWorker.class, 100, threads).run();
            new Runner(FlyingSaucerWorker.class, 20, threads).run(); // too slow, just loop 20 times
        }

    }
}
