package md.fusionworks.lifehack.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.util.Constants;

public class ExchangeRatesActivity extends NavigationDrawerActivity {


    public static Intent getCallingIntent(Context context) {

        Intent callingIntent = new Intent(context, ExchangeRatesActivity.class);
        return callingIntent;
    }

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
    public int getSelfDrawerItem() {

        return Constants.DRAWER_ITEM_EXCHANGE_RATES;
    }
}
