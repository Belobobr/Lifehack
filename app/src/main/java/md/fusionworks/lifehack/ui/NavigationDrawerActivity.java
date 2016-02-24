package md.fusionworks.lifehack.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.ArrayList;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.UIUtil;

/**
 * Created by admin on 03.09.2015.
 */
public class NavigationDrawerActivity extends BaseActivity {

  protected static final int DRAWER_ITEM_INVALID = -1;
  protected static final int DRAWER_ITEM_SEPARATOR = -2;

  private static final int DRAWER_LAUNCH_DELAY = 250;

  private static final int[] DRAWER_TITLE_RES_ID = new int[] {
      R.string.drawer_item_main, R.string.drawer_item_life_hacks,
      R.string.drawer_item_exchange_rates, R.string.drawer_item_life_movies,
      R.string.drawer_item_life_taxi, R.string.drawer_item_prices, R.string.drawer_item_sales,
      R.string.drawer_item_life_poster
      // R.string.drawer_item_settings
  };

  private static final int[] DRAWER_ICON_RES_ID = new int[] {
      R.drawable.ic_home, R.drawable.ic_newspaper, R.drawable.ic_currency_exchange,
      R.drawable.ic_movie, R.drawable.ic_taxi, R.drawable.ic_price_tag, R.drawable.ic_sale,
      R.drawable.ic_advertising,
      //R.drawable.ic_lens_black_24dp,
  };

  private static final boolean[] DRAWER_ITEM_ACTIVE_STATE = new boolean[] {
      true, true, true, false, true, false, true, false,
      //false
  };

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
  @Bind(R.id.drawerItemsContainer) ViewGroup drawerItemsListContainer;

  private ArrayList<Integer> drawerItems = new ArrayList<Integer>();
  private View[] drawerItemViews = null;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private Handler handler;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    handler = new Handler();
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    setupDrawerLayout();
  }

  private void setupDrawerLayout() {
    if (drawerLayout == null) {
      return;
    }

    actionBarDrawerToggle =
        new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

    drawerLayout.setDrawerListener(actionBarDrawerToggle);
    toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
    actionBarDrawerToggle.syncState();

    populateDrawerItems();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    actionBarDrawerToggle.onConfigurationChanged(newConfig);
  }

  public void setTitle(String title) {
    getSupportActionBar().setTitle(title);
  }

  private void populateDrawerItems() {
    drawerItems.add(Constant.DRAWER_ITEM_MAIN);
    drawerItems.add(Constant.DRAWER_ITEM_LIFE_HACKS);
    drawerItems.add(Constant.DRAWER_ITEM_EXCHANGE_RATES);
    drawerItems.add(Constant.DRAWER_ITEM_SALES);
    drawerItems.add(Constant.DRAWER_ITEM_TAXI);
    drawerItems.add(Constant.DRAWER_ITEM_PRICES);
    drawerItems.add(Constant.DRAWER_ITEM_MOVIES);
    drawerItems.add(Constant.DRAWER_ITEM_POSTER);
    // drawerItems.add(Constant.DRAWER_ITEM_SETTINGS);

    createDrawerItems();
  }

  private void createDrawerItems() {
    if (drawerItemsListContainer == null) {
      return;
    }

    drawerItemViews = new View[drawerItems.size()];
    drawerItemsListContainer.removeAllViews();
    int i = 0;
    for (int itemId : drawerItems) {
      drawerItemViews[i] = makeDrawerItem(itemId, drawerItemsListContainer);
      drawerItemsListContainer.addView(drawerItemViews[i]);
      ++i;
    }
  }

  private View makeDrawerItem(final int itemId, ViewGroup container) {
    boolean selected = getSelfDrawerItem() == itemId;
    int layoutToInflate = 0;
    if (itemId == DRAWER_ITEM_SEPARATOR) {
      layoutToInflate = R.layout.navdrawer_separator;
    } else {
      layoutToInflate = R.layout.navdrawer_item;
    }
    View view = getLayoutInflater().inflate(layoutToInflate, container, false);

    if (isSeparator(itemId)) {

      UIUtil.setAccessibilityIgnore(view);
      return view;
    }

    ImageView iconView = (ImageView) view.findViewById(R.id.icon);
    TextView titleView = (TextView) view.findViewById(R.id.title);
    int iconId = itemId >= 0 && itemId < DRAWER_ICON_RES_ID.length ? DRAWER_ICON_RES_ID[itemId] : 0;
    int titleId =
        itemId >= 0 && itemId < DRAWER_TITLE_RES_ID.length ? DRAWER_TITLE_RES_ID[itemId] : 0;
    boolean isActive =
        itemId >= 0 && itemId < DRAWER_ITEM_ACTIVE_STATE.length ? DRAWER_ITEM_ACTIVE_STATE[itemId]
            : false;

    iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
    if (iconId > 0) {
      iconView.setImageResource(iconId);
    }
    titleView.setText(getString(titleId));

    formatDrawerItem(view, itemId, selected, isActive);

    if (isActive) view.setOnClickListener(v -> onDrawerItemClicked(itemId));

    return view;
  }

  public int getSelfDrawerItem() {
    return DRAWER_ITEM_INVALID;
  }

  private boolean isSeparator(int itemId) {
    return itemId == DRAWER_ITEM_SEPARATOR;
  }

  private boolean isSimpleActivity(int itemId) {
    return itemId == DRAWER_ITEM_INVALID;
  }

  private void formatDrawerItem(View view, int itemId, boolean selected, boolean isActive) {
    if (isSeparator(itemId)) {
      // not applicable
      return;
    }

    ImageView iconView = (ImageView) view.findViewById(R.id.icon);
    TextView titleView = (TextView) view.findViewById(R.id.title);

    if (selected) {
      view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
    }

    int titleColorRes;
    int iconColorRes;

    if (isActive) {

      titleColorRes =
          selected ? R.color.navdrawer_text_color_selected : R.color.navdrawer_text_color;
      iconColorRes = selected ? R.color.navdrawer_icon_tint_selected : R.color.navdrawer_icon_tint;
    } else {

      titleColorRes = R.color.navdrawer_text_color_inactive;
      iconColorRes = R.color.navdrawer_icon_tint_inactive;
    }

    titleView.setTextColor(getResources().getColor(titleColorRes));
    iconView.setColorFilter(getResources().getColor(iconColorRes));
  }

  private void onDrawerItemClicked(final int itemId) {
    if (itemId == getSelfDrawerItem()) {
      drawerLayout.closeDrawer(GravityCompat.START);
      return;
    }

    if (isSimpleActivity(itemId)) {
      goToDrawerItem(itemId);
    } else {

      handler.postDelayed(() -> goToDrawerItem(itemId), DRAWER_LAUNCH_DELAY);

      setSelectedDrawerItem(itemId);
    }

    drawerLayout.closeDrawer(GravityCompat.START);
  }

  private void setSelectedDrawerItem(int itemId) {
    if (drawerItemViews != null) {
      for (int i = 0; i < drawerItemViews.length; i++) {
        if (i < drawerItems.size()) {

          int thisItemId = drawerItems.get(i);
          boolean isActive = itemId >= 0 && itemId < DRAWER_ITEM_ACTIVE_STATE.length
              ? DRAWER_ITEM_ACTIVE_STATE[itemId] : false;
          formatDrawerItem(drawerItemViews[i], thisItemId, itemId == thisItemId, isActive);
        }
      }
    }
  }

  public void goToDrawerItem(int item) {
    switch (item) {
      case Constant.DRAWER_ITEM_MAIN:
        getNavigator().navigateToMainActivity(this);
        finish();
        break;
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
