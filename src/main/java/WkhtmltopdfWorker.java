import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;

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
        pdf.addParam(new Param("--no-outline"));
        pdf.addParam(new Param("--lowquality"));
        if (optionalPath != null) {
            pdf.saveAs(optionalPath);
        }
    }

    public static void main(String[] args) throws Exception {
        new WkhtmltopdfWorker(1, null).doTest("target/wk_html.pdf");
    }
}
