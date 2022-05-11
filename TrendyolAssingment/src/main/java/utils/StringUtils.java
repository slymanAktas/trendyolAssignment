package utils;

import java.util.Random;

public class StringUtils {
    public static String getRandomString(int charCount){
        Random random = new Random();
        return random.ints(97, 122)
                .limit(charCount)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
