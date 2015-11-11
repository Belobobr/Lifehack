package md.fusionworks.lifehack.exchange_rates.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Currency;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepositoryImpl;

/**
 * Created by ungvas on 11/11/15.
 */
public class GetCurrenciesImpl implements GetCurrencies {

    private ExchangeRatesRepository exchangeRatesRepository;

    public GetCurrenciesImpl(Context context) {
        this(new ExchangeRatesRepositoryImpl(context));
    }

    GetCurrenciesImpl(ExchangeRatesRepository exchangeRatesRepository) {

        this.exchangeRatesRepository = exchangeRatesRepository;
    }


    @Override
    public void execute(@NonNull Callback<List<Currency>> callback) {

        exchangeRatesRepository.getCurrencies(new Callback<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> response) {

                callback.onSuccess(response);
            }

            @Override
            public void onError(Throwable t) {

                callback.onError(t);
            }
        });
    }
}
