package md.fusionworks.lifehack.application

import dagger.Component
import md.fusionworks.lifehack.activity.BaseActivity
import md.fusionworks.lifehack.util.rx.RxBusDagger
import javax.inject.Singleton

/**
 * Created by ungvas on 3/17/16.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

  fun inject(baseActivity: BaseActivity)

  fun provideRxBus(): RxBusDagger
}