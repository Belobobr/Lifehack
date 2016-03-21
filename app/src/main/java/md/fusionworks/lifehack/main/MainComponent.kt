package md.fusionworks.lifehack.main

import dagger.Component
import md.fusionworks.lifehack.application.AppComponent
import md.fusionworks.lifehack.di.PerActivity

/**
 * Created by ungvas on 3/21/16.
 */
@PerActivity
@Component(dependencies = arrayOf(AppComponent::class))
interface MainComponent {

  fun inject(mainActivity: MainActivity)
}