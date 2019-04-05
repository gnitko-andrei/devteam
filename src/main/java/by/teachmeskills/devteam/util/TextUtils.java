package by.teachmeskills.devteam.util;

public class TextUtils {
    public static String replaceHyphenationOnBr(String text) {
        return text.replaceAll("\n", "<br>");
    }

    public static String replaceBrOnHyphenation(String text) {
        return text.replaceAll("<br>", "\n");
    }
}
