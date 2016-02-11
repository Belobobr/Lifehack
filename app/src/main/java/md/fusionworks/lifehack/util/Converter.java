package md.fusionworks.lifehack.util;

import android.text.TextUtils;

/**
 * Created by ungvas on 11/2/15.
 */
public class Converter {

  public static double toDouble(String value) {

    if (TextUtils.isEmpty(value)) return 0;

    return Double.valueOf(value);
  }
}
