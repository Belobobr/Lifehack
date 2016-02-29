package md.fusionworks.lifehack.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by ungvas on 2/29/16.
 */
public class AndroidUtil {

  public static boolean isPackageInstalled(String packageName, Context context) {
    PackageManager pm = context.getPackageManager();
    try {
      pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }
}
