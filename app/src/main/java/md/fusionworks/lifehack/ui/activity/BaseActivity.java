package md.fusionworks.lifehack.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import md.fusionworks.lifehack.application.LifehackApplication;
import md.fusionworks.lifehack.di.HasComponent;
import md.fusionworks.lifehack.di.component.ActivityComponent;
import md.fusionworks.lifehack.di.component.ApplicationComponent;
import md.fusionworks.lifehack.di.component.DaggerActivityComponent;
import md.fusionworks.lifehack.di.module.ActivityModule;
import md.fusionworks.lifehack.navigation.Navigator;

/**
 * Created by ungvas on 10/15/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected ActivityComponent activityComponent;
    @Inject Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeInjector();
    }

    protected void addFragment(int containerViewId, Fragment fragment) {

        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    private void initializeInjector() {

        activityComponent = DaggerActivityComponent
                .builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        activityComponent.inject(this);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((LifehackApplication) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
