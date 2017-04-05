package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getCurrentDateTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }
}