package md.fusionworks.lifehack.taxi

import dagger.Component
import md.fusionworks.lifehack.application.AppComponent
import md.fusionworks.lifehack.PerActivity

/**
 * Created by ungvas on 3/18/16.
 */
@PerActivity
@Component(modules = arrayOf(TaxiModule::class), dependencies = arrayOf(AppComponent::class))
interface TaxiComponent {

  fun inject(taxiActivity: TaxiActivity)
}