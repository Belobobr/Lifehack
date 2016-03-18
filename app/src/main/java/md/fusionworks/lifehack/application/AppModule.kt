package md.fusionworks.lifehack.application

import android.app.Application
import dagger.Module
import dagger.Provides
import md.fusionworks.lifehack.util.rx.RxBusDagger
import javax.inject.Singleton

/**
 * Created by ungvas on 3/17/16.
 */
@Module
class AppModule(application: Application) {

  @Provides
  @Singleton
  fun provideRxBus(): RxBusDagger {
    return RxBusDagger()
  }

}