package md.fusionworks.lifehack.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import md.fusionworks.lifehack.ui.exchange_rates.ExchangeRatesActivity;
import md.fusionworks.lifehack.ui.main.MainActivity;
import md.fusionworks.lifehack.ui.sales.SalesActivity;
import md.fusionworks.lifehack.ui.taxi.TaxiActivity;

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

  public void navigateToSalesActivity(Context context) {
    Intent intentToLaunch = new Intent(context, SalesActivity.class);
    context.startActivity(intentToLaunch);
  }

  public void navigateToCallActivity(Context context, int phoneNumber) {
    Intent intentToLaunch = new Intent(Intent.ACTION_DIAL);
    intentToLaunch.setData(Uri.parse(String.format("tel:%d", phoneNumber)));
    context.startActivity(intentToLaunch);
  }
}
