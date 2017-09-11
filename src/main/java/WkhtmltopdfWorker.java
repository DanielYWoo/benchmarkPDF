import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;

import java.util.concurrent.CountDownLatch;

public class WkhtmltopdfWorker extends BaseWorker {

    private static final String html = HTMLUtil.getLongContent();

    public WkhtmltopdfWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    @Override
    void doTest(String optionalPath) throws Exception {
        Pdf pdf = new Pdf();
        pdf.addPageFromString(html);
        pdf.addToc();
        if (optionalPath != null) {
            pdf.saveAs(optionalPath);
        }
    }

    public static void main(String[] args) throws Exception {
        new WkhtmltopdfWorker(1, null).doTest("target/wk_html.pdf");
    }
}
