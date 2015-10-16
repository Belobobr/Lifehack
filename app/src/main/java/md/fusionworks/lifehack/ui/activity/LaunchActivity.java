package md.fusionworks.lifehack.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import md.fusionworks.lifehack.presenter.LaunchPresenter;
import md.fusionworks.lifehack.ui.view.LaunchView;

public class LaunchActivity extends BaseActivity implements LaunchView {

    private LaunchPresenter launchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.launchPresenter = new LaunchPresenter();
        this.launchPresenter.attachView(this);
        this.launchPresenter.goToApplication();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.launchPresenter.destroy();
    }
}
