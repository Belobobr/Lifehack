package md.fusionworks.lifehack.application

import dagger.Component
import io.realm.Realm
import md.fusionworks.lifehack.rx.RxBusDagger
import md.fusionworks.lifehack.view.activity.BaseActivity
import javax.inject.Singleton

/**
 * Created by ungvas on 3/17/16.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

  fun inject(baseActivity: BaseActivity)

  fun provideRxBus(): RxBusDagger

  fun provideRealm(): Realm
}