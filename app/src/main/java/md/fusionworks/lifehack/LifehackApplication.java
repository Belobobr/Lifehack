package md.fusionworks.lifehack;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.LocaleHelper;

/**
 * Created by ungvas on 10/20/15.
 */
public class LifehackApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    String currentLanguage = LocaleHelper.getLanguage(this);
    if (currentLanguage.equals(Constant.LANG_EN)) LocaleHelper.onCreate(this, Constant.LANG_RU);

    if (BuildConfig.USE_CRASHLYTICS) {
      Fabric.with(this, new Crashlytics());
    }

    LeakCanary.install(this);

    RealmConfiguration config = new RealmConfiguration.Builder(this).build();
    Realm.setDefaultConfiguration(config);

    Stetho.initializeWithDefaults(this);
  }
}
