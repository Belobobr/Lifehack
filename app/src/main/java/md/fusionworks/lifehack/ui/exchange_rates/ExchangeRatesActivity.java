package md.fusionworks.lifehack.ui.exchange_rates;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.widget.FrameLayout;
import butterknife.Bind;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.NavigationDrawerActivity;
import md.fusionworks.lifehack.ui.exchange_rates.event.ScrollToMapEvent;
import md.fusionworks.lifehack.ui.exchange_rates.event.WhereToBuyEvent;
import md.fusionworks.lifehack.ui.exchange_rates.fragment.BranchListFragment;
import md.fusionworks.lifehack.ui.exchange_rates.fragment.BranchMapFragment;
import md.fusionworks.lifehack.ui.exchange_rates.fragment.ExchangeRatesFragment;
import md.fusionworks.lifehack.util.Constant;

public class ExchangeRatesActivity extends NavigationDrawerActivity {

  @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
  @Bind(R.id.container) NestedScrollView container;
  @Bind(R.id.branchMapContainer) FrameLayout branchMapContainer;
  @Bind(R.id.exchangeRatesContainer) FrameLayout exchangeRatesContainer;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exchange_rates);

    addFragment(R.id.exchangeRatesContainer, ExchangeRatesFragment.newInstance());
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle(getString(R.string.title_exchange_rates));
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_EXCHANGE_RATES;
  }

  @Override protected void listenForEvents() {
    super.listenForEvents();
    getRxBus().event(WhereToBuyEvent.class)
        .compose(this.bindToLifecycle())
        .subscribe(whereToBuyEvent -> {
          addFragment(R.id.branchMapContainer,
              BranchMapFragment.newInstance(whereToBuyEvent.getBranchModelList()));
          addFragment(R.id.branchListContainer,
              BranchListFragment.newInstance(whereToBuyEvent.getBranchModelList()));
        });

    getRxBus().event(ScrollToMapEvent.class)
        .compose(this.bindToLifecycle())
        .subscribe(scrollToMapEvent -> {
          container.smoothScrollTo(0, exchangeRatesContainer.getBottom());
        });
  }
}
