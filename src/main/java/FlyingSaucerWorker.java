import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class FlyingSaucerWorker extends BaseWorker {

    private final ITextRenderer renderer;

    public FlyingSaucerWorker(int loop, CountDownLatch latch) throws Exception {
        super(loop, latch);
        long t1 = System.currentTimeMillis();
        renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("fonts/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        fontResolver.addFont("fonts/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        fontResolver.addFont("fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        fontResolver.addFont("fonts/simkai.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        System.out.println("flying saucer init:" + (System.currentTimeMillis() - t1));
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
