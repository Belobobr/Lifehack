package md.fusionworks.lifehack.exchange_rates.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Bank;

/**
 * Created by ungvas on 11/10/15.
 */
public interface GetBanks {

    void execute(@NonNull Callback<List<Bank>> callback);
}
