package md.fusionworks.lifehack.navigation;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Named;

import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.ui.activity.ExchangeRatesActivity;

/**
 * Created by ungvas on 10/19/15.
 */
@PerActivity
public class Navigator {

    private Context context;

    @Inject
    public Navigator(@Named("activity") Context context) {

        this.context = context;
    }

    public void navigateToExchangeRatesActivity() {

        Intent intentToLaunch = new Intent(context, ExchangeRatesActivity.class);
        context.startActivity(intentToLaunch);
    }
}
