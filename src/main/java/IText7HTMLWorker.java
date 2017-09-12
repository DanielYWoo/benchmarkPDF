import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKey;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class IText7HTMLWorker extends BaseWorker {

    private static final String html = HTMLUtil.getLongContent();
    private static final FontProvider fontProvider;

    static {
        InputStream LICENSE_KEY = IText7HTMLWorker.class.getClassLoader().getResourceAsStream("itextkey.xml");
        LicenseKey.loadLicenseFile(LICENSE_KEY);
        String uri = IText7HTMLWorker.class.getClassLoader().getResource("fonts").getPath();
        fontProvider = new FontProvider();
        fontProvider.addDirectory(uri);
    }

    public IText7HTMLWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }

    public void doTest(String dest) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setFontProvider(fontProvider);

        PdfWriter pdfWriter;
        if (dest == null) {
            pdfWriter = new PdfWriter(new ByteArrayOutputStream());
        } else {
            pdfWriter = new PdfWriter(new File(dest));
        }
        HtmlConverter.convertToPdf(html, pdfWriter, converterProperties);
    }

    public static void main(String[] args) throws IOException {
        new IText7HTMLWorker(1, null).doTest("target/itext7_html.pdf");
    }

}
