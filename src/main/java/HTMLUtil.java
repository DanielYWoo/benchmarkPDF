import java.util.ArrayList;
import java.util.List;

public class HTMLUtil {

    private static final List<String> elements = new ArrayList<>();
    private static final String html = create();

    public static String create() {
        String str = "This is a tset asdfasdf asdf sad safd  asdf asdf sda fsad fsadf sdf ";
//            String str = "This is a 测试 asdfasdf asdf sad safd  asdf asdf sda fsad fsadf sdf ";
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        for (int i = 0; i < 4000; i++) {
            sb.append("<p>");
            sb.append(i + " " + str);
            sb.append("</p>");
            elements.add(i + " " + str);
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    public static List<String> getLongContentElements() {
        return elements;
    }

    public static String getLongContent() {
        return html;
    }

}
