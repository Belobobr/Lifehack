package md.fusionworks.lifehack.ui.navigator;

import android.content.Context;
import android.content.Intent;

import md.fusionworks.lifehack.ui.activity.ExchangeRatesActivity;

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
}
