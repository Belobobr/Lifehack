package md.fusionworks.lifehack.repository;

import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.data.net.Callback;
import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.Branch;
import md.fusionworks.lifehack.model.Currency;
import md.fusionworks.lifehack.model.Rate;

/**
 * Created by ungvas on 11/10/15.
 */
public interface ExchangeRatesRepository {

    void getBanks(@NonNull Callback<List<Bank>> callback);

    void getCurrencies(@NonNull Callback<List<Currency>> callback);

    void getRates(String date, @NonNull Callback<List<Rate>> callback);

    void getBankBranches(int bankId, boolean active, @NonNull Callback<List<Branch>> callback);
}
