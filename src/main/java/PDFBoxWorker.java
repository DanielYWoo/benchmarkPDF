import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFBoxWorker extends BaseWorker {
    static {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    public PDFBoxWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }


    void doTest(String optionalPath) throws Exception {

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
            try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(100, 700);
                contents.showText(HTMLUtil.getLongContent());
                contents.endText();
            }
            if (optionalPath != null) {
                doc.save(optionalPath);
            } else {
                doc.save(new ByteArrayOutputStream());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new PDFBoxWorker(1, null).doTest("target/test_pdfbox.pdf");
    }
}
