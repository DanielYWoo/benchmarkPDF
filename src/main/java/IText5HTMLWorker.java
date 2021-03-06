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


    public IText5HTMLWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }
    static {
//        FontFactory.registerDirectory(IText5HTMLWorker.class.getClassLoader().getResource("fonts").getPath());
    }

    public void doTest(String optionalPath) throws Exception {
        Document document = new Document(PageSize.A4, 30.0F, 30.0F, 60.0F, 60.0F);
        PdfWriter writer;
        if (optionalPath != null) {
            writer = PdfWriter.getInstance(document, new BufferedOutputStream(new FileOutputStream(optionalPath)));
        } else {
            writer = PdfWriter.getInstance(document, new ByteArrayOutputStream());
        }
        document.open();

        ElementList elements = parseHtml(HTMLUtil.getLongContent(), Tags.getHtmlTagProcessorFactory());
        for (Element e : elements) {
            document.add(e);
        }
        document.close();
        writer.close();
    }

    private ElementList parseHtml(String content, TagProcessorFactory tagProcessors) throws IOException {
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
        p.parse(new StringReader(content));
        return elements;
    }

    public static void main(String[] args) throws Exception {
        new IText5HTMLWorker(1, null).doTest("target/test_itext_from_html.pdf");
    }

}