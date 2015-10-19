package md.fusionworks.lifehack.ui.activity;

import android.content.Context;
import android.os.Bundle;

import md.fusionworks.lifehack.presenter.LaunchPresenter;
import md.fusionworks.lifehack.ui.view.LaunchView;

public class LaunchActivity extends BaseActivity implements LaunchView {

    private LaunchPresenter launchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        launchPresenter.goToApplication();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        launchPresenter.destroy();
    }

    private void initialize() {

        launchPresenter = new LaunchPresenter();
        launchPresenter.attachView(this);
    }
}
