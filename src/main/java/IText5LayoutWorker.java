import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;

public class IText5LayoutWorker extends BaseWorker {

    public IText5LayoutWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    public void doTest(String optionalPath) throws Exception {
        Document document = new Document(new Rectangle(595.32F, 841.92F), 90.0F, 32.7F, 160.4F, 50.7F);
        PdfWriter writer;
        if (optionalPath != null) {
            writer = PdfWriter.getInstance(document, new BufferedOutputStream(new FileOutputStream(optionalPath)));
        } else {
            writer = PdfWriter.getInstance(document, new ByteArrayOutputStream());
        }
        document.open();
        for (String text : HTMLUtil.getLongContentElements()) {
            Paragraph e = new Paragraph(text);
            document.add(e);
        }
        document.close();
        writer.close();
    }

    public static void main(String[] args) throws Exception {
        new IText5LayoutWorker(1, null).doTest("target/test_itext_layout.pdf");
    }

}