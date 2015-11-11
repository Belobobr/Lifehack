package md.fusionworks.lifehack.exchange_rates.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Branch;

/**
 * Created by ungvas on 11/11/15.
 */
public interface GetBankBranches {

    void execute(int bankId, boolean active, @NonNull Callback<List<Branch>> callback);
}
