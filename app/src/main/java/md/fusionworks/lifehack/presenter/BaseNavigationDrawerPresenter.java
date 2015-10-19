package md.fusionworks.lifehack.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import md.fusionworks.lifehack.ui.activity.ExchangeRatesActivity;
import md.fusionworks.lifehack.ui.view.BaseNavigationDrawerView;
import md.fusionworks.lifehack.util.Constants;

/**
 * Created by ungvas on 10/18/15.
 */
public class BaseNavigationDrawerPresenter implements Presenter<BaseNavigationDrawerView> {

    private BaseNavigationDrawerView baseNavigationDrawerView;

    @Override
    public void attachView(@NonNull BaseNavigationDrawerView view) {

        baseNavigationDrawerView = view;
    }

    @Override
    public void detachView(@NonNull BaseNavigationDrawerView view) {

        baseNavigationDrawerView = null;
    }

    @Override
    public void destroy() {

        this.detachView(baseNavigationDrawerView);
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

                Context context = baseNavigationDrawerView.getContext();
                intentToLaunch = new Intent(context, ExchangeRatesActivity.class);
                context.startActivity(intentToLaunch);
                ((Activity) context).finish();
                break;
        }
    }

    public void onDrawerItemClicked(final int itemId) {

        baseNavigationDrawerView.onDrawerItemClicked(itemId);
    }

    public void openDrawer() {

        baseNavigationDrawerView.openDrawer();
    }

    public void closeDrawer() {

        baseNavigationDrawerView.closeDrawer();
    }
}
