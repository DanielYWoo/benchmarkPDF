
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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

public class TestiText {

    private static final ElementList elements = parseHtml(HTMLUtil.getLongContent(), Tags.getHtmlTagProcessorFactory());

    public void createPdf() throws IOException, DocumentException {
        System.out.println();
        long t1 = System.currentTimeMillis();
        // step 1
        Document document = new Document(new Rectangle(595.32F, 841.92F), 90.0F, 32.7F, 160.4F, 50.7F);
        System.out.println(System.currentTimeMillis() - t1);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new ByteArrayOutputStream());
        // PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(result));
        // step 3
        document.open();
        System.out.println(System.currentTimeMillis() - t1);
        t1 = System.currentTimeMillis();
        // step 4
        for (Element e : elements) {
            document.add(e);
        }
        // step 5
        System.out.println(System.currentTimeMillis() - t1);
        document.close();
        writer.close();
    }

    public static ElementList parseHtml(String content, TagProcessorFactory tagProcessors) {
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

    public static void main(String[] args) throws IOException, DocumentException {
        int loop = 100;
        long t1 = System.currentTimeMillis();
        for (int i  = 0; i < loop; i++) {
            System.out.println("loop " + i);
            new TestiText().createPdf();
        }
        long duration = System.currentTimeMillis() - t1;
        System.out.println();
        System.out.println("time:" + duration);
        System.out.println("TPS:" + ((double) loop/duration*1000));
        System.out.println("latency:" + ((double) duration/loop));
    }

}