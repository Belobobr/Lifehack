package md.fusionworks.lifehack.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import md.fusionworks.lifehack.data.net.Callback;
import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.repository.ExchangeRatesRepositoryImpl;

/**
 * Created by ungvas on 11/10/15.
 */

public class GetBanksImpl implements GetBanks {

    private ExchangeRatesRepository exchangeRatesRepository;

    @Inject
    public GetBanksImpl(ExchangeRatesRepository exchangeRatesRepository) {

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
