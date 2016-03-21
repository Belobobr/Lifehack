package md.fusionworks.lifehack.taxi.persistence

import dagger.Module
import dagger.Provides
import io.realm.Realm
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.taxi.TaxiRepository

/**
 * Created by ungvas on 3/21/16.
 */
@Module
class TaxiRepositoryModule {

  @Provides
  @PerActivity
  fun provideTaxiRepository(realm: Realm, taxiDataStore: TaxiDataStore) = TaxiRepository(
      realm, taxiDataStore)
}