package md.fusionworks.lifehack.exchange_rates.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Currency;

/**
 * Created by ungvas on 11/11/15.
 */
public interface GetCurrencies {

    void execute(@NonNull Callback<List<Currency>> callback);
}
