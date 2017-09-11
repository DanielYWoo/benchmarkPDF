import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.CountDownLatch;

public class ITextLayoutWorker extends BaseWorker {

    public ITextLayoutWorker(int loop, CountDownLatch latch) {
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
        new ITextLayoutWorker(1, null).doTest("target/test_itext_layout.pdf");
    }

}