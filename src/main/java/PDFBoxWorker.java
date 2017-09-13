import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

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
            PDType0Font font = PDType0Font.load(doc, this.getClass().getClassLoader().getResourceAsStream("fonts/ARIALUNI.TTF"));
            try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                int i = 100;
                for (String element : HTMLUtil.getLongContentElements()) {
                    contents.beginText();
                    contents.setFont(font, 10);
                    contents.newLineAtOffset(100, i);
                    contents.showText(element.replace("\r", "").replace("\n", ""));
                    contents.endText();
                    i += 30;
                }
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
