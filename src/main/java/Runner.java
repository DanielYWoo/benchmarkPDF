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
            if (clazz == IText5HTMLWorker.class) {
                workers[i] = new IText5HTMLWorker(loops, latch);
            } else if (clazz == IText5LayoutWorker.class) {
                workers[i] = new IText5LayoutWorker(loops, latch);
            } else if (clazz == FlyingSaucerWorker.class) {
                workers[i] = new FlyingSaucerWorker(loops, latch);
            } else if (clazz == PDFBoxWorker.class) {
                workers[i] = new PDFBoxWorker(loops, latch);
            } else if (clazz == PDFBoxLayoutWorker.class) {
                workers[i] = new PDFBoxLayoutWorker(loops, latch);
            } else if (clazz == IText7HTMLWorker.class) {
                workers[i] = new IText7HTMLWorker(loops, latch);
            } else if (clazz == IText2HTMLWorker.class) {
                workers[i] = new IText2HTMLWorker(loops, latch);
            } else if (clazz == WkhtmltopdfWorker.class) {
                workers[i] = new WkhtmltopdfWorker(loops, latch);
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
        int initialThreads = 1;
        int defaultLoops = 10;
        if (args.length != 0) {
            initialThreads = Integer.parseInt(args[0]);
            defaultLoops = Integer.parseInt(args[1]);
        }
        // warm up
        System.out.println("================== Warm up ===========================");
        new File("target").mkdir();
        generateHtmlFile("target/test.html");
        new WkhtmltopdfWorker(1, null).doTest("target/wk_html.pdf");
        new IText5HTMLWorker(1, null).doTest("target/itext5_html.pdf");
        new IText7HTMLWorker(1, null).doTest("target/itext7_html.pdf");
        new IText2HTMLWorker(1, null).doTest("target/itext2_html.pdf");
        new IText5LayoutWorker(1, null).doTest("target/itext5_layout.pdf");
//        new PDFBoxWorker(1, null).doTest("target/pdfbox.pdf");
//        new PDFBoxLayoutWorker(1, null).doTest("target/pdfbox_layout.pdf");
        new FlyingSaucerWorker(1, null).doTest("target/flying_saucer.pdf");

        Map<Class, List<Double>> throughput = new HashMap<>();
        throughput.put(WkhtmltopdfWorker.class, new ArrayList<>());
        throughput.put(IText5HTMLWorker.class, new ArrayList<>());
        throughput.put(IText2HTMLWorker.class, new ArrayList<>());
        throughput.put(IText5LayoutWorker.class, new ArrayList<>());
//        throughput.put(PDFBoxWorker.class, new ArrayList<>());
//        throughput.put(PDFBoxLayoutWorker.class, new ArrayList<>());
        throughput.put(FlyingSaucerWorker.class, new ArrayList<>());

        for (int threads = initialThreads; threads <= 64; threads *= 2) {
            // start
            System.out.println("================== Test with " + threads + " threads ==================");
//            throughput.get(WkhtmltopdfWorker.class).add( new Runner(WkhtmltopdfWorker.class, defaultLoops / threads, threads).run());
//            throughput.get(IText5HTMLWorker.class).add( new Runner(IText5HTMLWorker.class, defaultLoops / threads, threads).run());
//            throughput.get(IText2HTMLWorker.class).add( new Runner(IText2HTMLWorker.class, defaultLoops / threads, threads).run());
//            throughput.get(IText5LayoutWorker.class).add(new Runner(IText5LayoutWorker.class, defaultLoops / threads, threads).run());
//            throughput.get(PDFBoxWorker.class).add(new Runner(PDFBoxWorker.class, defaultLoops / threads, threads).run());
//            throughput.get(PDFBoxLayoutWorker.class).add(new Runner(PDFBoxLayoutWorker.class, 10, threads).run()); // too slow
//            throughput.get(IText7HTMLWorker.class).add( new Runner(IText7HTMLWorker.class, 100 / threads, threads).run()); // too slow
//            throughput.get(FlyingSaucerWorker.class).add(new Runner(FlyingSaucerWorker.class, 10, threads).run()); // too slow
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
