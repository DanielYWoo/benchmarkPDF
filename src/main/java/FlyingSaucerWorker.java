import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class FlyingSaucerWorker extends BaseWorker {

    private static ITextRenderer renderer = new ITextRenderer();

    public FlyingSaucerWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    void doTest(String optionalPath) throws Exception {
        renderer.setDocumentFromString(HTMLUtil.getLongContent());
        renderer.layout();
        OutputStream out;
        if (optionalPath != null) {
            out = new BufferedOutputStream(new FileOutputStream(optionalPath));
        } else {
            out = new ByteArrayOutputStream();
        }
        renderer.createPDF(out);
        renderer.finishPDF();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        new FlyingSaucerWorker(1, null).doTest("target/test_flying_saucer.pdf");
    }
}
