package md.fusionworks.lifehack.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import butterknife.Bind;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.fragment.ExchangeRatesFragment;
import md.fusionworks.lifehack.util.Constant;

public class ExchangeRatesActivity extends NavigationDrawerActivity {

  @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exchange_rates);

    addFragment(R.id.currencyConverterFragment, ExchangeRatesFragment.newInstance());
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle(getString(R.string.module_title_exchange_rates));
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_EXCHANGE_RATES;
  }
}
