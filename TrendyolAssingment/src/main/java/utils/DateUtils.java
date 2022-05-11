package utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ofPattern;

public class DateUtils {
    public static String getDateNow() {
        LocalDateTime localDate = LocalDateTime.now();
        return localDate.format(ofPattern("ddMMMyyyy HHmmss"));
    }

    public static String getDateNow(String format) {
        return new SimpleDateFormat(format).format(new Date());//format should be as "dd/MM/yyyy" ,"dd/MM/yy" ,"MM/dd/yyyy"
    }
}
