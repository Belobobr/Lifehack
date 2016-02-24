package md.fusionworks.lifehack;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import md.fusionworks.lifehack.data.persistence.taxi.TaxiData;
import md.fusionworks.lifehack.data.persistence.taxi.TaxiDataStore;
import md.fusionworks.lifehack.data.repository.TaxiRepository;

/**
 * Created by ungvas on 10/20/15.
 */
public class LifehackApplication extends Application {

  private TaxiRepository taxiRepository;
  private Realm realm;

  @Override public void onCreate() {
    super.onCreate();
    if (BuildConfig.USE_CRASHLYTICS) {
      Fabric.with(this, new Crashlytics());
    }

    LeakCanary.install(this);

    RealmConfiguration config = new RealmConfiguration.Builder(this).build();
    Realm.setDefaultConfiguration(config);
    realm = Realm.getDefaultInstance();

    taxiRepository = new TaxiRepository(realm, new TaxiDataStore(realm));
    if (!taxiRepository.hasData()) taxiRepository.add(TaxiData.TAXI_PHONE_NUMBER_LIST);
  }
}
