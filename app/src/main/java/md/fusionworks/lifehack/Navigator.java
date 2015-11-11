package md.fusionworks.lifehack;

import android.content.Context;
import android.content.Intent;

import md.fusionworks.lifehack.exchange_rates.ExchangeRatesActivity;

/**
 * Created by ungvas on 10/19/15.
 */

public class Navigator {

    private Context context;

    public Navigator(Context context) {

        this.context = context;
    }

    public void navigateToExchangeRatesActivity() {

        Intent intentToLaunch = new Intent(context, ExchangeRatesActivity.class);
        context.startActivity(intentToLaunch);
    }
}
