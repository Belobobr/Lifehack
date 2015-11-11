package md.fusionworks.lifehack.exchange_rates.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Bank;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepositoryImpl;

/**
 * Created by ungvas on 11/10/15.
 */

public class GetBanksImpl implements GetBanks {

    private ExchangeRatesRepository exchangeRatesRepository;

    public GetBanksImpl(Context context) {
        this(new ExchangeRatesRepositoryImpl(context));
    }

    GetBanksImpl(ExchangeRatesRepository exchangeRatesRepository) {

        this.exchangeRatesRepository = exchangeRatesRepository;
    }

    @Override
    public void execute(@NonNull Callback<List<Bank>> callback) {

        exchangeRatesRepository.getBanks(new Callback<List<Bank>>() {
            @Override
            public void onSuccess(List<Bank> response) {

                callback.onSuccess(response);
            }

            @Override
            public void onError(Throwable t) {

                callback.onError(t);
            }
        });
    }
}
