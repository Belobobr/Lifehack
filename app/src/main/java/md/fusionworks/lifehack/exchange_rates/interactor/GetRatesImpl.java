package md.fusionworks.lifehack.exchange_rates.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Rate;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepositoryImpl;

/**
 * Created by ungvas on 11/11/15.
 */
public class GetRatesImpl implements GetRates {

    private ExchangeRatesRepository exchangeRatesRepository;

    public GetRatesImpl(Context context) {
        this(new ExchangeRatesRepositoryImpl(context));
    }

    GetRatesImpl(ExchangeRatesRepository exchangeRatesRepository) {

        this.exchangeRatesRepository = exchangeRatesRepository;
    }


    @Override
    public void execute(String date, @NonNull Callback<List<Rate>> callback) {

        exchangeRatesRepository.getRates(date, new Callback<List<Rate>>() {
            @Override
            public void onSuccess(List<Rate> response) {

                callback.onSuccess(response);
            }

            @Override
            public void onError(Throwable t) {

                callback.onError(t);
            }
        });
    }
}
