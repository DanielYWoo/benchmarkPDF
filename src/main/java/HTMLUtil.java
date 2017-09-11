import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class HTMLUtil {

    private static List<String> elements;
    private static String html;

    static {
        URL sample = HTMLUtil.class.getClassLoader().getResource("sample.html");
        StringBuilder contentBuilder = new StringBuilder();
        if (sample != null) {
            try (Stream<String> stream = Files.lines(Paths.get(sample.getPath()), StandardCharsets.UTF_8)) {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        html = contentBuilder.toString();
        elements = Jsoup.parse(html).body().select("*").stream().map(Element::ownText).collect(Collectors.toList());
    }

    public static List<String> getLongContentElements() {
        return elements;
    }

    public static String getLongContent() {
        return html;
    }

}
