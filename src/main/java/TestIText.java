
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.html.Tags;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestIText {

    public static final String PDF =  "d:/tmp/test/stationery.pdf";

    private static final ElementList elements;
    static {
        try {
            elements = FillTemplateHelper.parseHtml(HTMLUtil.getLongContent(), Tags.getHtmlTagProcessorFactory());
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public void createPdf(String result) throws IOException, DocumentException {
        System.out.println();
        long t1 = System.currentTimeMillis();
        FillTemplateHelper template = new FillTemplateHelper(PDF);
        // step 1
        Document document = new Document(template.getPageSize(),
                template.getmLeft(), template.getmRight(), template.getmTop(), template.getmBottom());
        System.out.println(System.currentTimeMillis() - t1);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new ByteArrayOutputStream());
        // PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(result));
        //  writer.setPageEvent(template);
        // step 3
        document.open();
        System.out.println(System.currentTimeMillis() - t1);

        t1 = System.currentTimeMillis();
        // step 4
        // ElementList elements = FillTemplateHelper.parseHtml(HTML, CSS, Tags.getHtmlTagProcessorFactory());
        for (Element e : elements) {
            document.add(e);
        }
        // step 5
        System.out.println(System.currentTimeMillis() - t1);
        document.close();
        writer.close();
    }

    public static void main(String[] args) throws IOException, DocumentException {
        int loop = 100;
        long t1 = System.currentTimeMillis();
        for (int i  = 0; i < loop; i++) {
            System.out.println("loop " + i);
            String name = "d:/tmp/test/results/" + i+ ".PDF";
            new TestIText().createPdf(name);
        }
        long duration = System.currentTimeMillis() - t1;
        System.out.println();
        System.out.println("time:" + duration);
        System.out.println("TPS:" + ((double) loop/duration*1000));
        System.out.println("latency:" + ((double) duration/loop));
    }
}