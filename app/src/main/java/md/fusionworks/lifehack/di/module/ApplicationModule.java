package md.fusionworks.lifehack.di.module;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import md.fusionworks.lifehack.application.LifehackApplication;
import md.fusionworks.lifehack.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.repository.ExchangeRatesRepositoryImpl;

/**
 * Created by ungvas on 11/10/15.
 */

@Module
public class ApplicationModule {

    private LifehackApplication lifehackApplication;

    public ApplicationModule(LifehackApplication lifehackApplication) {
        this.lifehackApplication = lifehackApplication;
    }

    @Provides
    @Singleton
    @Named("application")
    Context provideApplicationContext() {
        return this.lifehackApplication;
    }

    @Provides
    @Singleton
    ExchangeRatesRepository provideExchangeRatesRepository(ExchangeRatesRepositoryImpl exchangeRatesRepository) {

        return exchangeRatesRepository;
    }
}
