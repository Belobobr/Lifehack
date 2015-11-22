package md.fusionworks.lifehack.exchange_rates;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.navigation_drawer.NavigationDrawerActivity;
import md.fusionworks.lifehack.util.Constants;

public class ExchangeRatesActivity extends NavigationDrawerActivity {

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rates);
        ButterKnife.bind(this);
        addFragment(R.id.currencyConverterFragment, ExchangeRatesFragment.newInstance());
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

    public void showNotificationToast(String message, ExchangeRatesView.NotificationToastType type) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);

        switch (type) {

            case INFO:
                break;
            case ERROR:

                View view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                break;
        }

        snackbar.show();
    }
}
