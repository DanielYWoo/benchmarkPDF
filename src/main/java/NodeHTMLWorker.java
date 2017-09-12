import java.util.concurrent.CountDownLatch;

public class NodeHTMLWorker extends BaseWorker {

    private static final String htmlPath = HTMLUtil.class.getClassLoader().getResource("sample.html").getPath();

    public NodeHTMLWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    @Override
    void doTest(String optionalPath) throws Exception {
        optionalPath = "target/node_html.pdf";
        String cmd = "html-pdf " + htmlPath + " " + optionalPath;
        Process process = Runtime.getRuntime().exec(cmd);
//        process.waitFor();
    }

    public static void main(String[] args) throws Exception {
        new NodeHTMLWorker(1, null).doTest("target/node_html.pdf");
    }
}
