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
            } else if (clazz == PDFBoxWorker.class) {
                workers[i] = new PDFBoxWorker(loops, latch);
            } else if (clazz == PDFBoxLayoutWorker.class) {
                workers[i] = new PDFBoxLayoutWorker(loops, latch);
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
        new ITextWorker(1, null).doTest(null);
        new PDFBoxWorker(1, null).doTest(null);
        new PDFBoxLayoutWorker(1, null).doTest(null);
        new FlyingSaucerWorker(1, null).doTest(null);


        for (int threads = 1; threads < 32; threads *= 2) {
            // start
            System.out.println("================== Test with " + threads + "threads ==================");
            new Runner(ITextWorker.class, 100, threads).run();
            new Runner(PDFBoxWorker.class, 20, threads).run();
            new Runner(PDFBoxLayoutWorker.class, 20, threads).run(); // too slow, just loop 20 times
            new Runner(FlyingSaucerWorker.class, 20, threads).run(); // too slow, just loop 20 times
        }

    }
}
