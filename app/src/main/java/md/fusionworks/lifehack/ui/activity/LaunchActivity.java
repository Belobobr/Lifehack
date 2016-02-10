package md.fusionworks.lifehack.ui.activity;

import android.os.Bundle;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigator().navigateToExchangeRatesActivity(this);
        finish();
    }
}