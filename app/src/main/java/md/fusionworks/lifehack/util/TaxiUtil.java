package md.fusionworks.lifehack.util;

import java.util.Date;

/**
 * Created by ungvas on 2/18/16.
 */
public class TaxiUtil {

  public static boolean wasUsedRecently(Date lastUsedDate) {
    if (lastUsedDate == null) return false;

    long secs = (new Date().getTime() - lastUsedDate.getTime()) / 1000;
    long mins = secs / 60;
    return mins < Constant.TAXI_PHONE_NUMBER_RECENTLY_USED_TIME;
  }
}
