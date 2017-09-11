import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HTMLUtil {

    private static final List<String> elements = new ArrayList<>();
    private static final String html = create();

    public static String create() {
        URL sample = HTMLUtil.class.getClassLoader().getResource("sample.html");
        StringBuilder contentBuilder = new StringBuilder();
        if (sample != null) {
            try (Stream<String> stream = Files.lines(Paths.get(sample.getPath()), StandardCharsets.UTF_8)) {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return contentBuilder.toString();
    }

    public static List<String> getLongContentElements() {
        return elements;
    }

    public static String getLongContent() {
        return html;
    }

}
