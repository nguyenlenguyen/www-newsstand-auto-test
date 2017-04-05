package utilities;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

public class StringUtils {
    private static String random(String type, int length) {
        String base = type.equals("string") ? "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ" : "0123456789";
        String s = "";
        Random rand = new Random();
        while (s.length() < length) {
            s += base.charAt(rand.nextInt(base.length()));
        }
        return s;
    }

    public static String randomString(String prefix, int length) {
        return prefix + random("string", length);
    }

    public static String randomString(int length, String suffix) {
        return random("string", length) + suffix;
    }

    public static String randomNumber(String prefix, int length) {
        return prefix + random("number", length);
    }

    public static String[] addStringToArray(String[] stringArray, String stringToAdd) {
        return ArrayUtils.add(stringArray, stringToAdd);
    }

    public static String getLastSubString(String string, String separator) {
        String[] tmp = string.split(separator);
        return tmp[tmp.length - 1];
    }

    public static String getRightString(String string, int length) {
        return string.substring(Math.max(0, string.length() - length));
    }
}