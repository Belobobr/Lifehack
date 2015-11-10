package md.fusionworks.lifehack.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Named;

import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.navigation.Navigator;
import md.fusionworks.lifehack.ui.view.NavigationDrawerView;
import md.fusionworks.lifehack.util.Constants;

/**
 * Created by ungvas on 10/18/15.
 */
@PerActivity
public class NavigationDrawerPresenter implements Presenter<NavigationDrawerView> {

    private Context context;
    private Navigator navigator;
    private NavigationDrawerView navigationDrawerView;

    @Inject
    public NavigationDrawerPresenter(@Named("activity") Context context, Navigator navigator) {

        this.context = context;
        this.navigator = navigator;
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
