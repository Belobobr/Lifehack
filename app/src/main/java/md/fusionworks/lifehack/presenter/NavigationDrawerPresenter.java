package md.fusionworks.lifehack.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import md.fusionworks.lifehack.navigation.Navigator;
import md.fusionworks.lifehack.ui.activity.ExchangeRatesActivity;
import md.fusionworks.lifehack.ui.view.NavigationDrawerView;
import md.fusionworks.lifehack.util.Constants;

/**
 * Created by ungvas on 10/18/15.
 */
public class NavigationDrawerPresenter implements Presenter<NavigationDrawerView> {

    private Context context;
    private Navigator navigator;
    private NavigationDrawerView navigationDrawerView;

    public NavigationDrawerPresenter(Context context) {

        this.context = context;
        navigator = new Navigator(context);
    }

    @Override
    public void attachView(@NonNull NavigationDrawerView view) {

        navigationDrawerView = view;
    }

    @Override
    public void detachView(@NonNull NavigationDrawerView view) {

        navigationDrawerView = null;
    }

    @Override
    public void destroy() {

        this.detachView(navigationDrawerView);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public void goToDrawerItem(int item) {

        Intent intentToLaunch;
        switch (item) {

            case Constants.DRAWER_ITEM_EXCHANGE_RATES:

                navigator.navigateToExchangeRatesActivity();
                ((Activity) context).finish();
                break;
        }
    }

    public void onDrawerItemClicked(final int itemId) {

        navigationDrawerView.onDrawerItemClicked(itemId);
    }

    public void openDrawer() {

        navigationDrawerView.openDrawer();
    }

    public void closeDrawer() {

        navigationDrawerView.closeDrawer();
    }
}
