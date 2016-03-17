package md.fusionworks

import dagger.Component
import md.fusionworks.lifehack.ui.base.view.BaseActivity
import md.fusionworks.lifehack.util.rx.RxBusDagger
import javax.inject.Singleton

/**
 * Created by ungvas on 3/17/16.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
public interface AppComponent {

  fun inject(baseActivity: BaseActivity)

  fun provideRxBus(): RxBusDagger
}