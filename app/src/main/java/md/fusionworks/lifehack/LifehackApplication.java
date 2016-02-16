package md.fusionworks.lifehack;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by ungvas on 10/20/15.
 */
public class LifehackApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    LeakCanary.install(this);
  }
}
