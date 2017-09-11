import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public double run() throws InterruptedException {
        System.out.println();
        System.out.println(this.clazz.getSimpleName());
        System.out.println("Run " + (loops * threads) + " times with " + threads + " threads");
        long t1 = System.currentTimeMillis();
        for (BaseWorker worker : workers) {
            worker.start();
        }
        latch.await();
        long duration = System.currentTimeMillis() - t1;
        double tps = ((double) loops * threads / duration * 1000);
        System.out.println();
        System.out.println("Duration(ms):" + duration);
        System.out.println("TPS:" + tps);
        System.out.println("Average Latency:" + ((double) duration / loops / threads));

        System.gc();
        Thread.sleep(1000);
        return tps;
    }

    public static void main(String[] args) throws Exception {
        // warm up
        System.out.println("================== Warm up ===========================");
        new File("target").mkdir();
        generateHtmlFile("target/test.html");
        new ITextHTMLWorker(1, null).doTest("target/itext_html.pdf");
        new ITextLayoutWorker(1, null).doTest("target/itext_layout.pdf");
        new PDFBoxWorker(1, null).doTest("target/pdfbox.pdf");
        new PDFBoxLayoutWorker(1, null).doTest("target/pdfbox_layout.pdf");
        new FlyingSaucerWorker(1, null).doTest("target/flying_saucer.pdf");

        Map<Class, List<Double>> throughput = new HashMap<>();
        throughput.put(ITextHTMLWorker.class, new ArrayList<>());
        throughput.put(ITextLayoutWorker.class, new ArrayList<>());
        throughput.put(PDFBoxWorker.class, new ArrayList<>());
        throughput.put(PDFBoxLayoutWorker.class, new ArrayList<>());
        throughput.put(FlyingSaucerWorker.class, new ArrayList<>());

        for (int threads = 1; threads <= 64; threads *= 2) {
            // start
            System.out.println("================== Test with " + threads + " threads ==================");
            throughput.get(ITextHTMLWorker.class).add( new Runner(ITextHTMLWorker.class, 1024 / threads, threads).run());
            throughput.get(ITextLayoutWorker.class).add(new Runner(ITextLayoutWorker.class, 1024 / threads, threads).run());
            throughput.get(PDFBoxWorker.class).add(new Runner(PDFBoxWorker.class, 1024 / threads, threads).run());
            throughput.get(PDFBoxLayoutWorker.class).add(new Runner(PDFBoxLayoutWorker.class, 10, threads).run()); // too slow
            throughput.get(FlyingSaucerWorker.class).add(new Runner(FlyingSaucerWorker.class, 10, threads).run()); // too slow
        }

        System.out.println("================== throughput summary ==================");
        for (Class clazz : throughput.keySet()) {
            System.out.println(clazz.getSimpleName() + ": " + throughput.get(clazz));
        }

    }

    private static void generateHtmlFile(String path) throws IOException {
        File file = new File(path);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write(HTMLUtil.getLongContent());
        }
    }

}
