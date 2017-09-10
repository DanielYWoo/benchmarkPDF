import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;

public class FlyingSaucerWorker extends BaseWorker {

    private static ITextRenderer renderer = new ITextRenderer();

    public FlyingSaucerWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    void doTest() {
        try {
            renderer.setDocumentFromString(HTMLUtil.getLongContent());
            renderer.layout();
            renderer.createPDF( new ByteArrayOutputStream());
            renderer.finishPDF();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
