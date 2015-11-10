package md.fusionworks.lifehack.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.data.net.Callback;
import md.fusionworks.lifehack.model.Bank;

/**
 * Created by ungvas on 11/10/15.
 */
public interface GetBanks {

    void execute(@NonNull Callback<List<Bank>> callback);
}
