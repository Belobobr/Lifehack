package md.fusionworks.lifehack.taxi

import dagger.Component
import md.fusionworks.lifehack.application.AppComponent
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.taxi.persistence.TaxiDataStoreModule

/**
 * Created by ungvas on 3/18/16.
 */
@PerActivity
@Component(
    modules = arrayOf(TaxiDataStoreModule::class),
    dependencies = arrayOf(AppComponent::class))
interface TaxiComponent {

  fun inject(taxiActivity: TaxiActivity)
}