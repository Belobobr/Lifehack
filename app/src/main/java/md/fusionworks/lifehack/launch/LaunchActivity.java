package md.fusionworks.lifehack.launch;

import android.os.Bundle;

import md.fusionworks.lifehack.ui.BaseActivity;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator.navigateToExchangeRatesActivity();
    }
}