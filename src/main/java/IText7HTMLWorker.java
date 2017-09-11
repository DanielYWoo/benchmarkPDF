import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public class IText7HTMLWorker extends BaseWorker {

    private static final String html = HTMLUtil.getLongContent();

    static {
        InputStream LICENSE_KEY = IText7HTMLWorker.class.getClassLoader().getResourceAsStream("itextkey.xml");
        LicenseKey.loadLicenseFile(LICENSE_KEY);
    }

    public IText7HTMLWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    public void doTest(String dest) throws IOException {
        PdfWriter pdfWriter;
        if (dest == null) {
            pdfWriter = new PdfWriter(new ByteArrayOutputStream());
        } else {
            pdfWriter = new PdfWriter(new File(dest));
        }
        HtmlConverter.convertToPdf(html, pdfWriter);
    }

}
