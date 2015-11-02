package md.fusionworks.lifehack.di.component;

import android.content.Context;

import dagger.Component;
import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.di.module.ActivityModule;
import md.fusionworks.lifehack.ui.activity.BaseActivity;
import md.fusionworks.lifehack.ui.activity.NavigationDrawerActivity;

/**
 * Created by ungvas on 10/21/15.
 */
@PerActivity
@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(BaseActivity baseActivity);
    void inject(NavigationDrawerActivity navigationDrawerActivity);

    Context context();
}