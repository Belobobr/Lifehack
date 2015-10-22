package md.fusionworks.lifehack.di.component;

import dagger.Component;
import md.fusionworks.lifehack.di.module.ActivityModule;
import md.fusionworks.lifehack.di.module.ExchangeRatesModule;
import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.di.scope.PerFragment;
import md.fusionworks.lifehack.ui.fragment.ExchangeRatesFragment;

/**
 * Created by ungvas on 10/22/15.
 */
@PerActivity
@Component(modules = {ActivityModule.class, ExchangeRatesModule.class})
public interface ExchangeRatesComponent extends ActivityComponent {

    void inject(ExchangeRatesFragment exchangeRatesFragment);
}
