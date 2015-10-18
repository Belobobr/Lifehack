package md.fusionworks.lifehack.ui.activity;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.presenter.BaseNavigationDrawerPresenter;
import md.fusionworks.lifehack.ui.view.BaseNavigationDrawerView;
import md.fusionworks.lifehack.util.Constants;
import md.fusionworks.lifehack.util.UIUtils;

/**
 * Created by admin on 03.09.2015.
 */
public class BaseNavigationDrawerActivity extends BaseActivity implements BaseNavigationDrawerView {

    protected static final int DRAWER_ITEM_INVALID = -1;
    protected static final int DRAWER_ITEM_SEPARATOR = -2;

    private static final int DRAWER_LAUNCH_DELAY = 250;

    private static final int[] DRAWER_TITLE_RES_ID = new int[]{

            R.string.drawer_item_exchange_rates
    };

    private static final int[] DRAWER_ICON_RES_ID = new int[]{

            R.drawable.ic_attach_money_black_24dp
    };

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawerItemsContainer)
    ViewGroup drawerItemsListContainer;

    private ArrayList<Integer> drawerItems = new ArrayList<Integer>();
    private View[] drawerItemViews = null;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Handler handler;
    private BaseNavigationDrawerPresenter baseNavigationDrawerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        handler = new Handler();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerLayout();
    }

    private void initialize() {

        this.baseNavigationDrawerPresenter = new BaseNavigationDrawerPresenter();
        this.baseNavigationDrawerPresenter.attachView(this);
    }

    @Override
    public void setupDrawerLayout() {

        int selfItem = getSelfDrawerItem();

        if (drawerLayout == null) {
            return;
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        actionBarDrawerToggle.syncState();

        populateDrawerItems();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(String title) {

        getSupportActionBar().setTitle(title);
    }

    @Override
    public void populateDrawerItems() {

        drawerItems.add(Constants.DRAWER_ITEM_EXCHANGE_RATES);

        createDrawerItems();
    }

    @Override
    public void createDrawerItems() {

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

    @Override
    public View makeDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == DRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {

            UIUtils.setAccessibilityIgnore(view);
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < DRAWER_ICON_RES_ID.length ?
                DRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < DRAWER_TITLE_RES_ID.length ?
                DRAWER_TITLE_RES_ID[itemId] : 0;

        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatDrawerItem(view, itemId, selected);

        view.setOnClickListener(v -> onDrawerItemClicked(itemId));

        return view;
    }

    @Override
    public int getSelfDrawerItem() {
        return DRAWER_ITEM_INVALID;
    }

    @Override
    public boolean isSeparator(int itemId) {
        return itemId == DRAWER_ITEM_SEPARATOR;
    }

    @Override
    public boolean isSimpleActivity(int itemId) {
        return itemId == DRAWER_ITEM_INVALID;
    }

    @Override
    public void formatDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        if (selected) {
            view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
        }

        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    @Override
    public void onDrawerItemClicked(final int itemId) {
        if (itemId == getSelfDrawerItem()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (isSimpleActivity(itemId)) {
            this.baseNavigationDrawerPresenter.goToDrawerItem(itemId);
        } else {

            handler.postDelayed(() ->
                    this.baseNavigationDrawerPresenter.goToDrawerItem(itemId), DRAWER_LAUNCH_DELAY);

            setSelectedDrawerItem(itemId);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public void setSelectedDrawerItem(int itemId) {

        if (drawerItemViews != null) {
            for (int i = 0; i < drawerItemViews.length; i++) {
                if (i < drawerItems.size()) {
                    int thisItemId = drawerItems.get(i);
                    formatDrawerItem(drawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.baseNavigationDrawerPresenter.destroy();
    }


    @Override
    public Context getContext() {
        return this;
    }
}
