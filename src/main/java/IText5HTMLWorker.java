import com.itextpdf.text.*;
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

public class IText5HTMLWorker extends BaseWorker {

    private static final ElementList elements = parseHtml(HTMLUtil.getLongContent(), Tags.getHtmlTagProcessorFactory());

    public IText5HTMLWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }
    static {
        FontFactory.registerDirectory(IText5HTMLWorker.class.getClassLoader().getResource("fonts").getPath());
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
        for (Element e : elements) {
            document.add(e);
        }
        document.close();
        writer.close();
    }

    private static ElementList parseHtml(String content, TagProcessorFactory tagProcessors) {
        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        // CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(""));
        // cssResolver.addCss(cssFile);
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(tagProcessors);
        htmlContext.autoBookmark(false);
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline html = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);

        try {
            p.parse(new StringReader(content));
        } catch (IOException e) {
            throw new Error(e);
        }
        return elements;
    }

    public static void main(String[] args) throws Exception {
        new IText5HTMLWorker(1, null).doTest("target/test_itext_from_html.pdf");
    }

}