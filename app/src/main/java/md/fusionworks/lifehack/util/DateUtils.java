package md.fusionworks.lifehack.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ungvas on 10/29/15.
 */
public class DateUtils {

    private static SimpleDateFormat rateDateFormat;
    private static SimpleDateFormat dateViewFormat;

    public static SimpleDateFormat getRateDateFormat() {
        if (rateDateFormat == null) {
            rateDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        }
        return rateDateFormat;
    }

    public static SimpleDateFormat getDateViewFormat() {
        if (dateViewFormat == null) {
            dateViewFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        }
        return dateViewFormat;
    }

    public static Date createDate(int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }
}
