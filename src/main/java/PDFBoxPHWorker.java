import com.helger.font.api.EFontStyle;
import com.helger.font.api.EFontType;
import com.helger.font.api.FontResource;
import com.helger.font.api.FontWeight;
import com.helger.font.api.IFontStyle;
import com.helger.pdflayout4.PageLayoutPDF;
import com.helger.pdflayout4.base.PLPageSet;
import com.helger.pdflayout4.element.text.PLText;
import com.helger.pdflayout4.spec.EHorzAlignment;
import com.helger.pdflayout4.spec.FontSpec;
import com.helger.pdflayout4.spec.PreloadFont;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PDFBoxPHWorker extends BaseWorker {
    static {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    public PDFBoxPHWorker(int loop, CountDownLatch latch) {
        super(loop, latch);
    }


    void doTest(String optionalPath) throws Exception {
        final FontSpec r10 = new FontSpec (PreloadFont.createNonEmbedding(new FontResource(
                "SimKai", EFontType.TTF, EFontStyle.REGULAR, new FontWeight(1), "fonts/simkai.ttf"
        )), 10);
        final PLPageSet aPS1 = new PLPageSet (PDRectangle.A4).setMargin (20, 30).setPadding (5);
        aPS1.setPageHeader (new PLText("page header here", r10).setHorzAlign (EHorzAlignment.CENTER));
        aPS1.setPageFooter (new PLText ("footer here ", r10).setHorzAlign (EHorzAlignment.CENTER));
        for (String s : HTMLUtil.getLongContentElements()) {
            aPS1.addElement (new PLText (s, r10));
        }
        final PageLayoutPDF aPageLayout = new PageLayoutPDF ();
        aPageLayout.addPageSet (aPS1);
        OutputStream out;
        if (optionalPath != null) {
            out = new FileOutputStream(optionalPath);
        } else {
            out = new ByteArrayOutputStream();
        }
        aPageLayout.renderTo(out);
    }


    public static void main(String[] args) throws Exception {
        new PDFBoxPHWorker(1, null).doTest("target/test_pdfbox_phlayout.pdf");
    }
}
