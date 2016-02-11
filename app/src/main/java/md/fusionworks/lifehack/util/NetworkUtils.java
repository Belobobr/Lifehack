package md.fusionworks.lifehack.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ungvas on 11/3/15.
 */
public class NetworkUtils {

  public static boolean isThereInternetConnection(Context context) {
    boolean isConnected;

    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

    return isConnected;
  }
}
