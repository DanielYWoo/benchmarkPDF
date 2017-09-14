import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.concurrent.CountDownLatch;

public class IText2HTMLWorker extends BaseWorker {

    static {
        FontFactory.registerDirectory(IText2HTMLWorker.class.getClassLoader().getResource("fonts").getPath());
    }

    final StyleSheet stylesheet = new StyleSheet();

    public IText2HTMLWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
        stylesheet.loadTagStyle(HtmlTags.PARAGRAPH, "color", "#FF0000");
        stylesheet.loadTagStyle(HtmlTags.PARAGRAPH, "padding", "100px");
        stylesheet.loadTagStyle(HtmlTags.DIV, "margin", "0");
        stylesheet.loadTagStyle(HtmlTags.DIV, "padding", "0");
        stylesheet.loadTagStyle(HtmlTags.SPAN, "color", "red");
    }

    @Override
    void doTest(String optionalPath) throws Exception {
        OutputStream outputStream;
        if (optionalPath != null) {
            outputStream = new FileOutputStream(optionalPath);
        } else {
            outputStream = new ByteArrayOutputStream();
        }
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        PdfWriter w = PdfWriter.getInstance(document, outputStream);
        HTMLWorker worker = new HTMLWorker(document);
        worker.setStyleSheet(stylesheet);
        document.open();
        worker.parse(new StringReader(HTMLUtil.getLongContent()));

        worker.close();
        document.close();
        w.close();
    }

    public static void main(String[] args) throws Exception {
        new IText2HTMLWorker(1, null).doTest("/Users/danielwu/Documents/test.pdf");
    }

}
