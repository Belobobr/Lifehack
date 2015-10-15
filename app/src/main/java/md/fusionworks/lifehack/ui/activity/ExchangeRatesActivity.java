package md.fusionworks.lifehack.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import md.fusionworks.lifehack.R;

public class ExchangeRatesActivity extends BaseNavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rates);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setTitle(getString(R.string.module_title_exchange_rates));
    }

    @Override
    protected int getSelfDrawerItem() {

        return DRAWER_ITEM_EXCHANGE_RATES;
    }
}
