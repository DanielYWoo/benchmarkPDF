import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestFlyingSaucer {

    private static ITextRenderer renderer = new ITextRenderer();

    public static void main(String[] args) throws IOException, DocumentException {
        int loop = 10;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            test();
        }
        long duration = System.currentTimeMillis() - t1;
        System.out.println();
        System.out.println("time:" + duration);
        System.out.println("TPS:" + ((double) loop/duration*1000));
        System.out.println("latency:" + ((double) duration/loop));
    }

    public static void test() throws IOException, DocumentException {
        renderer.setDocumentFromString(HTMLUtil.getLongContent());
        renderer.layout();
        renderer.createPDF( new ByteArrayOutputStream());
        System.out.print(".");
    }

}
