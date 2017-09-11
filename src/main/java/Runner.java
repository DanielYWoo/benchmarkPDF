import java.io.File;
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
            if (clazz == ITextHTMLWorker.class) {
                workers[i] = new ITextHTMLWorker(loops, latch);
            } else if (clazz == ITextLayoutWorker.class) {
                workers[i] = new ITextLayoutWorker(loops, latch);
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
        System.out.println("Run " + (loops * threads) + " times with " + threads + " threads");
        long t1 = System.currentTimeMillis();
        for (BaseWorker worker : workers) {
            worker.run();
        }
        latch.await();
        long duration = System.currentTimeMillis() - t1;
        System.out.println();
        System.out.println("Duration(ms):" + duration);
        System.out.println("TPS:" + ((double) loops * threads / duration * 1000));
        System.out.println("Average Latency:" + ((double) duration / loops / threads));

        System.gc();
        Thread.sleep(2000);
    }

    public static void main(String[] args) throws Exception {
        // warm up
        System.out.println("================== Warm up ===========================");
        new File("target").mkdir();
        new ITextHTMLWorker(1, null).doTest("target/itext_html.pdf");
        new ITextLayoutWorker(1, null).doTest("target/itext_layout.pdf");
        new PDFBoxWorker(1, null).doTest("target/pdfbox.pdf");
        new PDFBoxLayoutWorker(1, null).doTest("target/pdfbox_layout.pdf");
        new FlyingSaucerWorker(1, null).doTest("target/flying_saucer.pdf");


        for (int threads = 1; threads < 32; threads *= 2) {
            // start
            System.out.println("================== Test with " + threads + "threads ==================");
            new Runner(ITextHTMLWorker.class, 100 / threads, threads).run();
            new Runner(ITextLayoutWorker.class, 100 / threads, threads).run();
            new Runner(PDFBoxWorker.class, 100 / threads, threads).run();
            new Runner(PDFBoxLayoutWorker.class, 3, threads).run(); // too slow, just loop 3 times
            new Runner(FlyingSaucerWorker.class, 3, threads).run(); // too slow, just loop 3 times
        }

    }
}
