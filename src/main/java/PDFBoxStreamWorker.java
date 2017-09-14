import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PDFBoxStreamWorker extends BaseWorker {
    static {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    public PDFBoxStreamWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }


    void doTest(String optionalPath) throws Exception {

        PDDocument document = new PDDocument();
        // Create a new page and add to the document
        PDPage page = new PDPage();
        document.addPage(page);
        // Create a new font object selecting one of the PDF base fonts
        PDType0Font font = PDType0Font.load(document, this.getClass().getClassLoader().getResourceAsStream("fonts/simkai.ttf"));
        // Start a new content stream which will hold content to be created
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        List<String> elements = HTMLUtil.getLongContentElements();
        int y = 0;
        for (String element : elements) {
            // Define a text content stream using the selected font, moving the
            // cursor and showing the text "Hello World"
            contentStream.beginText();
            contentStream.setFont(font, 12);

            // Move to the start of the next line, offset from the start of the
            // current line by (150, 700).
            y += 20;
            contentStream.newLineAtOffset(0, y);

            // Shows the given text at the location specified by the current
            // text matrix.
            contentStream.showText(filterText(element));
            contentStream.endText();
        }

        // Make sure that the content stream is closed.
        contentStream.close();
        if (optionalPath != null) {
            document.save(new BufferedOutputStream(new FileOutputStream(optionalPath)));
        } else {
            document.save(new ByteArrayOutputStream());
        }
        // finally make sure that the document is properly closed.
        document.close();
    }

    private String filterText(String element) {
        return element.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll("\u00A0", " ");
    }

    public static void main(String[] args) throws Exception {
        new PDFBoxStreamWorker(1, null).doTest("target/test_pdfbox_stream.pdf");
    }
}
