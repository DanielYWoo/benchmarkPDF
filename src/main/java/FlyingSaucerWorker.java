import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class FlyingSaucerWorker extends BaseWorker {

    private ITextRenderer renderer = new ITextRenderer();

    public FlyingSaucerWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    void doTest(String optionalPath) throws Exception {
        renderer.setDocumentFromString(HTMLUtil.getLongContent());
        renderer.getFontResolver().addFont("fonts/ARIALUNI.TTF", false);
        renderer.getFontResolver().addFont("fonts/simkai.ttf", false);
        renderer.getFontResolver().addFont("fonts/SIMSUN.TTC", false);
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

}
