package md.fusionworks.lifehack.ui.navigator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import md.fusionworks.lifehack.ui.activity.ExchangeRatesActivity;
import md.fusionworks.lifehack.ui.activity.LifeHacksActivity;
import md.fusionworks.lifehack.ui.activity.MainActivity;
import md.fusionworks.lifehack.ui.activity.TaxiActivity;

/**
 * Created by ungvas on 10/19/15.
 */

public class Navigator {

  public Navigator() {
  }

  public void navigateToMainActivity(Context context) {
    Intent intentToLaunch = new Intent(context, MainActivity.class);
    context.startActivity(intentToLaunch);
  }

  public void navigateToLifeHacksActivity(Context context) {
    Intent intentToLaunch = new Intent(context, LifeHacksActivity.class);
    context.startActivity(intentToLaunch);
  }

  public void navigateToExchangeRatesActivity(Context context) {
    Intent intentToLaunch = new Intent(context, ExchangeRatesActivity.class);
    context.startActivity(intentToLaunch);
  }

  public void navigateToTaxiActivity(Context context) {
    Intent intentToLaunch = new Intent(context, TaxiActivity.class);
    context.startActivity(intentToLaunch);
  }

  public void navigateToCallActivity(Context context, int phoneNumber) {
    Intent intentToLaunch = new Intent(Intent.ACTION_DIAL);
    intentToLaunch.setData(Uri.parse(String.format("tel:%d", phoneNumber)));
    context.startActivity(intentToLaunch);
  }
}
