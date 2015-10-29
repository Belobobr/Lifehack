package md.fusionworks.lifehack.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by ungvas on 10/29/15.
 */
public class DateUtils {

    private static SimpleDateFormat rateDateFormat;

    public static SimpleDateFormat getRateDateFormat() {
        if (rateDateFormat == null) {
            rateDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        }
        return rateDateFormat;
    }
}
