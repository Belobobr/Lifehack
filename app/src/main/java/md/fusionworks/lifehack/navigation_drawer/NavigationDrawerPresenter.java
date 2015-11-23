package md.fusionworks.lifehack.navigation_drawer;

import android.app.Activity;
import android.content.Context;

import md.fusionworks.lifehack.Navigator;
import md.fusionworks.lifehack.util.Constants;

/**
 * Created by ungvas on 11/23/15.
 */
public class NavigationDrawerPresenter implements NavigationDrawerContract.UserActionsListener {

    private Context context;
    private Navigator navigator;

    public NavigationDrawerPresenter(Context context, Navigator navigator) {

        this.context = context;
        this.navigator = navigator;
    }

    @Override
    public void goToDrawerItem(int item) {

        switch (item) {

            case Constants.DRAWER_ITEM_EXCHANGE_RATES:

                navigator.navigateToExchangeRatesActivity();
                ((Activity) context).finish();
                break;
        }
    }
}
