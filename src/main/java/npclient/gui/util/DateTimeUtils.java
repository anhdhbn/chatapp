package npclient.gui.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    private static boolean isSameDay(Date date) {
        Calendar target = Calendar.getInstance();
        target.setTime(date);

        Calendar current = Calendar.getInstance();

        return target.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH)
                && target.get(Calendar.MONTH) == current.get(Calendar.MONTH)
                && target.get(Calendar.YEAR) == current.get(Calendar.YEAR);
    }

    public static String format(long time) {
        Date date = new Date(time);
        if (isSameDay(date)) {
            return SAME_DAY_FORMATTER.format(date);
        } else
            return DIFF_DAY_FORMATTER.format(date);
    }

    private static SimpleDateFormat SAME_DAY_FORMATTER = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat DIFF_DAY_FORMATTER = new SimpleDateFormat("dd-MM HH:mm");
}
