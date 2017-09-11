import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class IText2HTMLWorker extends BaseWorker {

    public IText2HTMLWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    @Override
    void doTest(String optionalPath) throws Exception {
        OutputStream outputStream;
        if (optionalPath != null) {
            outputStream = new FileOutputStream(optionalPath);
        } else {
            outputStream = new ByteArrayOutputStream();
        }
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        List<Element> objects = HTMLWorker.parseToList(new StringReader(HTMLUtil.getLongContent()), null, null);
        for (Element element : objects) {
            document.add(element);
        }
        document.close();
    }

}
