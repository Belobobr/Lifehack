package md.fusionworks.lifehack.ui.main;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.NavigationDrawerActivity;
import md.fusionworks.lifehack.util.Constant;

public class MainActivity extends NavigationDrawerActivity {

  @Bind(R.id.menuList) RecyclerView menuList;

  private MenuAdapter menuAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    menuAdapter = new MenuAdapter();
    initializeMenuList();
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle(getString(R.string.title_main));
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_MAIN;
  }

  @Override protected void listenForEvents() {
    super.listenForEvents();
    getRxBus().event(MenuItemClickEvent.class)
        .compose(bindToLifecycle())
        .subscribe(menuItemClickEvent -> onMenuItemClickEvent(menuItemClickEvent.itemId));
  }

  private void initializeMenuList() {
    menuList.setLayoutManager(new GridLayoutManager(this, 2));
    menuList.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.menu_item_offset_size));
    menuList.setAdapter(menuAdapter);
  }

  private void onMenuItemClickEvent(int itemId) {
    switch (itemId) {
      case Constant.DRAWER_ITEM_EXCHANGE_RATES:
        getNavigator().navigateToExchangeRatesActivity(this);
        finish();
        break;
      case Constant.DRAWER_ITEM_TAXI:
        getNavigator().navigateToTaxiActivity(this);
        finish();
        break;
      case Constant.DRAWER_ITEM_LIFE_HACKS:
        getNavigator().navigateToLifeHacksActivity(this);
        finish();
        break;
      case Constant.DRAWER_ITEM_SALES:
        getNavigator().navigateToSalesActivity(this);
        finish();
        break;
    }
  }
}
