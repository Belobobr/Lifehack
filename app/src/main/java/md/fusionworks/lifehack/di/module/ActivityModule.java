package md.fusionworks.lifehack.di.module;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import md.fusionworks.lifehack.di.scope.PerActivity;

/**
 * Created by ungvas on 10/21/15.
 */

@Module
public class ActivityModule {

    private Context context;

    public ActivityModule(Context context) {
        this.context = context;
    }

    @Provides
    @PerActivity
    @Named("activity")
    Context context() {

        return this.context;
    }
}
