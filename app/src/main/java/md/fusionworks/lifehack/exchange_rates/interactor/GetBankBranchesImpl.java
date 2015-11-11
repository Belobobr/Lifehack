package md.fusionworks.lifehack.exchange_rates.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Branch;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepositoryImpl;

/**
 * Created by ungvas on 11/11/15.
 */
public class GetBankBranchesImpl implements GetBankBranches {

    private ExchangeRatesRepository exchangeRatesRepository;

    public GetBankBranchesImpl(Context context) {
        this(new ExchangeRatesRepositoryImpl(context));
    }

    GetBankBranchesImpl(ExchangeRatesRepository exchangeRatesRepository) {

        this.exchangeRatesRepository = exchangeRatesRepository;
    }


    @Override
    public void execute(int bankId, boolean active, @NonNull Callback<List<Branch>> callback) {

        exchangeRatesRepository.getBankBranches(bankId, active, new Callback<List<Branch>>() {
            @Override
            public void onSuccess(List<Branch> response) {

                callback.onSuccess(response);
            }

            @Override
            public void onError(Throwable t) {

                callback.onError(t);
            }
        });
    }
}
