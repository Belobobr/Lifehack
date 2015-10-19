package md.fusionworks.lifehack.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import md.fusionworks.lifehack.ui.activity.ExchangeRatesActivity;
import md.fusionworks.lifehack.ui.view.LaunchView;

/**
 * Created by ungvas on 10/16/15.
 */
public class LaunchPresenter implements Presenter<LaunchView> {

    private LaunchView launchView;

    public LaunchPresenter() {
    }

    @Override
    public void attachView(@NonNull LaunchView view) {

        launchView = view;
    }

    @Override
    public void detachView(@NonNull LaunchView view) {

        launchView = null;
    }

    @Override
    public void destroy() {

        detachView(launchView);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public void goToApplication() {

        Context context = launchView.getContext();
        Intent intentToLaunch = new Intent(context, ExchangeRatesActivity.class);
        context.startActivity(intentToLaunch);
        ((Activity) context).finish();
    }
}
