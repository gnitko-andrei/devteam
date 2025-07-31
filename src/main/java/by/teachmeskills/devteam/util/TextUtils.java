package by.teachmeskills.devteam.util;

public class TextUtils {
    private TextUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String replaceHyphenationOnBr(String text) {
        return text.replace("\n", "<br>");
    }

    public static String replaceBrOnHyphenation(String text) {
        return text.replace("<br>", "\n");
    }
}
