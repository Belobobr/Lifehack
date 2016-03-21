package md.fusionworks.lifehack.taxi.persistence

import dagger.Module
import dagger.Provides
import io.realm.Realm
import md.fusionworks.lifehack.di.PerActivity

/**
 * Created by ungvas on 3/21/16.
 */
@Module
class TaxiDataStoreModule {

  @Provides
  @PerActivity
  fun provideTaxiDataStore(realm: Realm) = TaxiDataStore(realm)
}