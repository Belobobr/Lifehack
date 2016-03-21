package md.fusionworks.lifehack.launch

import dagger.Component
import md.fusionworks.lifehack.application.AppComponent
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.taxi.persistence.TaxiDataStoreModule
import md.fusionworks.lifehack.taxi.persistence.TaxiRepositoryModule

/**
 * Created by ungvas on 3/21/16.
 */
@PerActivity
@Component(
    modules = arrayOf(TaxiDataStoreModule::class, TaxiRepositoryModule::class),
    dependencies = arrayOf(AppComponent::class))
interface LaunchComponent {

  fun inject(launchActivity: LaunchActivity)
}