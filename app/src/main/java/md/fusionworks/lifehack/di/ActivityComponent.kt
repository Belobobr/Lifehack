package md.fusionworks.lifehack.launch

import dagger.Component
import md.fusionworks.lifehack.application.AppComponent
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.main.MainActivity
import md.fusionworks.lifehack.taxi.TaxiActivity

/**
 * Created by ungvas on 3/21/16.
 */
@PerActivity
@Component(dependencies = arrayOf(AppComponent::class))
interface ActivityComponent {

  fun inject(launchActivity: LaunchActivity)

  fun inject(mainActivity: MainActivity)

  fun inject(taxiActivity: TaxiActivity)
}