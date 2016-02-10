package md.fusionworks.lifehack.data.api;

import java.util.List;

import md.fusionworks.lifehack.data.api.model.Bank;
import md.fusionworks.lifehack.data.api.model.Branch;
import md.fusionworks.lifehack.data.api.model.Currency;
import md.fusionworks.lifehack.data.api.model.Rate;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ungvas on 10/28/15.
 */
public interface LifehackClient {

    String BASE_URL = "http://banks.digest.md/";

    String BANKS = "api/banks.json?_format=json";
    String RATES = "api/rates.json?_format=json";
    String CURRENCIES = "api/currencies.json?_format=json";
    String BRANCHES = "api/banks/{bank}/branches.json?_format=json";

    @GET(BANKS)
    Call<List<Bank>> getBanks();

    @GET(CURRENCIES)
    Call<List<Currency>> getCurrencies();

    @GET(RATES)
    Call<List<Rate>> getRates(@Query("date") String date);

    @GET(BRANCHES)
    Call<List<Branch>> getBankBranches(@Path("bank") int bank, @Query("active") boolean active);
}
