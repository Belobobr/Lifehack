package md.fusionworks.lifehack;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ungvas on 10/20/15.
 */
public class LifehackApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    if (BuildConfig.USE_CRASHLYTICS) {
      Fabric.with(this, new Crashlytics());
    }

    LeakCanary.install(this);

    RealmConfiguration config = new RealmConfiguration.Builder(this).build();
    Realm.setDefaultConfiguration(config);
  }
}
