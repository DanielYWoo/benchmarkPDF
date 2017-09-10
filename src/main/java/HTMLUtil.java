public class HTMLUtil {

    public static String getLongContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        for (int i = 0; i < 35000; i++) {
            sb.append("<p>");
            sb.append("This is a 测试 asdfasdf asdf sad safd  asdf asdf sda fsad fsadf sdf ");
            sb.append(System.currentTimeMillis());
            sb.append("</p>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }

}
