package md.fusionworks.lifehack.application

import dagger.Component
import io.realm.Realm
import md.fusionworks.lifehack.navigator.Navigator
import md.fusionworks.lifehack.rx.RxBusDagger
import javax.inject.Singleton

/**
 * Created by ungvas on 3/17/16.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

  fun provideRxBus(): RxBusDagger

  fun provideRealm(): Realm

  fun provideNavigator(): Navigator
}