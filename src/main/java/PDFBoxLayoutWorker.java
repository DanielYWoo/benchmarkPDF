import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;

public class PDFBoxLayoutWorker extends BaseWorker {
    static {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    public PDFBoxLayoutWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }


    void doTest(String optionalPath) throws Exception {
        Document document = new Document(40, 60, 40, 60);
        List<String> elements = HTMLUtil.getLongContentElements();
        for (String element : elements) {
            Paragraph paragraph1 = new Paragraph();
            paragraph1.addMarkup(element, 11F, BaseFont.Times);
            document.add(paragraph1);
        }
        if (optionalPath != null) {
            document.save(new BufferedOutputStream(new FileOutputStream(optionalPath)));
        } else {
            document.save(new ByteArrayOutputStream());
        }

    }

    public static void main(String[] args) throws Exception {
        new PDFBoxLayoutWorker(1, null).doTest("target/test_pdfbox_layout.pdf");
    }
}
