package md.fusionworks.lifehack.application

import android.app.Application
import dagger.Module
import dagger.Provides
import io.realm.Realm
import md.fusionworks.lifehack.navigator.Navigator
import md.fusionworks.lifehack.rx.RxBusDagger
import javax.inject.Singleton

/**
 * Created by ungvas on 3/17/16.
 */
@Module
class AppModule(application: Application) {

  @Provides
  @Singleton
  fun provideRxBus() = RxBusDagger()

  @Provides
  @Singleton
  fun provideRealm() = Realm.getDefaultInstance()

  @Provides
  @Singleton
  fun provideNavigator() = Navigator()
}