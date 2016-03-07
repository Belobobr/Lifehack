package md.fusionworks.lifehack

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.LocaleHelper

/**
 * Created by ungvas on 10/20/15.
 */
class LifehackApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    val currentLanguage = LocaleHelper.getLanguage(this)
    if (currentLanguage == Constant.LANG_EN) LocaleHelper.onCreate(this, Constant.LANG_RU)

    if (BuildConfig.USE_CRASHLYTICS) {
      Fabric.with(this, Crashlytics())
    }

    LeakCanary.install(this)

    val config = RealmConfiguration.Builder(this).build()
    Realm.setDefaultConfiguration(config)

    Stetho.initializeWithDefaults(this)
  }
}
