import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import java.io.FileOutputStream;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class WkhtmltopdfWorker extends BaseWorker {

    private static final String html = HTMLUtil.getLongContent();

    public WkhtmltopdfWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    @Override
    void doTest(String optionalPath) throws Exception {
        Pdf pdf = new Pdf();
        pdf.addParam(new Param("--no-outline"));
        pdf.addParam(new Param("--lowquality"));
//        pdf.addParam(new Param("--dpi" , "90"));
        pdf.addPageFromString(html);
        byte[] bytes = pdf.getPDF();
        if (optionalPath != null) {
            try (FileOutputStream out = new FileOutputStream(optionalPath)) {
                out.write(bytes);
            }
//            pdf.saveAs(optionalPath);
        }
    }

    public static void main(String[] args) throws Exception {
        new WkhtmltopdfWorker(1, null).doTest("target/wk_html.pdf");
    }


}
