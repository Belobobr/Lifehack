package md.fusionworks.lifehack.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.widget.ScrollView;
import butterknife.Bind;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.event.ScrollToEvent;
import md.fusionworks.lifehack.ui.fragment.ExchangeRatesFragment;
import md.fusionworks.lifehack.util.Constant;

public class ExchangeRatesActivity extends NavigationDrawerActivity {

  @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
  @Bind(R.id.container) ScrollView container;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exchange_rates);

    addFragment(R.id.container, ExchangeRatesFragment.newInstance());
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle(getString(R.string.module_title_exchange_rates));
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_EXCHANGE_RATES;
  }

  @Override protected void listenForEvents() {
    super.listenForEvents();
    getRxBus().event(ScrollToEvent.class)
        .compose(this.bindToLifecycle())
        .subscribe(scrollToEvent -> {
          container.smoothScrollTo(scrollToEvent.getX(), scrollToEvent.getY());
        });
  }
}
