import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class HTMLUtil {

    private static final List<String> elements;
    private static final String html;
    static {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    HTMLUtil.class.getClassLoader().getResourceAsStream("sample_no_nested.html"), "utf8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            html = sb.toString();
            elements = Jsoup.parse(html).body().select("*").stream().map(Element::ownText).collect(Collectors.toList());
        } catch (Exception e) {
            throw new Error(e);
        }
        System.out.println("HTML loaded " + (html.getBytes().length / 1024) + "KB");
    }

    public static List<String> getLongContentElements() {
        return elements;
    }

    public static String getLongContent() {
        return html;
    }

}
