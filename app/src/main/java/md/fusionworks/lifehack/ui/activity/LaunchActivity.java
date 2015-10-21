package md.fusionworks.lifehack.ui.activity;

import android.os.Bundle;

import javax.inject.Inject;

import md.fusionworks.lifehack.application.LifehackApplication;
import md.fusionworks.lifehack.navigation.Navigator;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator.navigateToExchangeRatesActivity();
        finish();
    }
}
