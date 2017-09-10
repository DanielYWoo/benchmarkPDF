
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.CountDownLatch;

public class ITextWorker extends BaseWorker {

    private static final ElementList elements = parseHtml(HTMLUtil.getLongContent(), Tags.getHtmlTagProcessorFactory());

    public ITextWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    public void doTest() {
        try {
            Document document = new Document(new Rectangle(595.32F, 841.92F), 90.0F, 32.7F, 160.4F, 50.7F);
            PdfWriter writer = PdfWriter.getInstance(document, new ByteArrayOutputStream());
            document.open();
            for (Element e : elements) {
                document.add(e);
            }
            document.close();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

}