package md.fusionworks.lifehack;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ungvas on 10/20/15.
 */
public class LifehackApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    LeakCanary.install(this);
  }
}
