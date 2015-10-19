package md.fusionworks.lifehack.ui.activity;

import android.os.Bundle;

import md.fusionworks.lifehack.navigation.Navigator;

public class LaunchActivity extends BaseActivity {

    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator = new Navigator(this);
        navigator.navigateToExchangeRatesActivity();
        finish();
    }
}
