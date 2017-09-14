import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

public class OpenHtmlWorker extends BaseWorker {

    private static final String html = HTMLUtil.getLongContent();
    private static final PdfRendererBuilder builder = new PdfRendererBuilder();
//    private static final String htmlPath = HTMLUtil.class.getClassLoader().getResource("sample.html").getPath();

//    static {
//        String url = IText2LayoutWorker.class.getClassLoader().getResource("fonts").getPath();
//        File fontFile = new File(url + "/SIMSUN.TTC");
//        builder.useFont(fontFile, "SimSun");
//    }

    public OpenHtmlWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    @Override
    void doTest(String optionalPath) throws Exception {
        builder.defaultTextDirection(PdfRendererBuilder.TextDirection.LTR);
        builder.withHtmlContent(html, "/tmp");
//        builder.withUri(htmlPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        builder.toStream(out);
        builder.run();
        if (optionalPath != null) {
            File file = new File(optionalPath);
            try(OutputStream outputStream = new FileOutputStream(file)) {
                out.writeTo(outputStream);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new OpenHtmlWorker(1, null).doTest("target/open_html.pdf");
    }
}
