package md.fusionworks.lifehack.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import md.fusionworks.lifehack.ui.activity.ExchangeRatesActivity;

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
        ((Activity) context).finish();
    }
}
