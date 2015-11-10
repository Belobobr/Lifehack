package md.fusionworks.lifehack.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.di.HasComponent;
import md.fusionworks.lifehack.di.component.DaggerExchangeRatesComponent;
import md.fusionworks.lifehack.di.component.ExchangeRatesComponent;
import md.fusionworks.lifehack.di.module.ExchangeRatesModule;
import md.fusionworks.lifehack.ui.fragment.ExchangeRatesFragment;
import md.fusionworks.lifehack.util.Constants;

public class ExchangeRatesActivity extends NavigationDrawerActivity implements HasComponent<ExchangeRatesComponent> {

    private ExchangeRatesComponent exchangeRatesComponent;

    public static Intent getCallingIntent(Context context) {

        Intent callingIntent = new Intent(context, ExchangeRatesActivity.class);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rates);

        addFragment(R.id.fragmentLayout, ExchangeRatesFragment.newInstance());
        initializeInjector();
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

    private void initializeInjector() {

        exchangeRatesComponent = DaggerExchangeRatesComponent
                .builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .exchangeRatesModule(new ExchangeRatesModule())
                .build();
    }

    @Override
    public ExchangeRatesComponent getComponent() {
        return exchangeRatesComponent;
    }
}
