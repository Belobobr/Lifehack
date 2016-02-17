package md.fusionworks.lifehack.ui.navigator;

import android.content.Context;
import android.content.Intent;
import md.fusionworks.lifehack.ui.activity.ExchangeRatesActivity;
import md.fusionworks.lifehack.ui.activity.TaxiActivity;

/**
 * Created by ungvas on 10/19/15.
 */

public class Navigator {

  public Navigator() {
  }

  public void navigateToExchangeRatesActivity(Context context) {
    Intent intentToLaunch = new Intent(context, ExchangeRatesActivity.class);
    context.startActivity(intentToLaunch);
  }

  public void navigateToTaxiActivity(Context context) {
    Intent intentToLaunch = new Intent(context, TaxiActivity.class);
    context.startActivity(intentToLaunch);
  }
}
