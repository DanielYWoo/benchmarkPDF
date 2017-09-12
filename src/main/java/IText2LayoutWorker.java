import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.concurrent.CountDownLatch;

public class IText2LayoutWorker extends BaseWorker {

    static {
        FontFactory.registerDirectory(IText2LayoutWorker.class.getClassLoader().getResource("fonts").getPath());
    }


    public IText2LayoutWorker(int loop, CountDownLatch latch) {
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
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        PdfWriter w = PdfWriter.getInstance(document, outputStream);
        document.open();
//        Font font = FontFactory.getFont("/Users/danielwu/Downloads/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        BaseFont baseFontChinese = BaseFont.createFont("/Users/danielwu/Downloads/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        Font font =  new  Font(baseFontChinese , 12 , Font.NORMAL);
        BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font =  new  Font(baseFontChinese , 12 , Font.NORMAL);
        for (String element : HTMLUtil.getLongContentElements()) {
            document.add(new Paragraph(element, font));
        }
        document.close();
        w.close();
    }


    public static void main(String[] args) throws Exception {
        new IText2LayoutWorker(1, null).doTest("/Users/danielwu/Documents/test.pdf");
    }

}
