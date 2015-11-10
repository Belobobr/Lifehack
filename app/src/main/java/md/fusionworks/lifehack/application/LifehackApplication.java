package md.fusionworks.lifehack.application;

import android.app.Application;

import md.fusionworks.lifehack.di.component.ApplicationComponent;
import md.fusionworks.lifehack.di.component.DaggerApplicationComponent;
import md.fusionworks.lifehack.di.module.ApplicationModule;


/**
 * Created by ungvas on 10/20/15.
 */
public class LifehackApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
