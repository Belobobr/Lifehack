package md.fusionworks.lifehack.data.net;

import java.util.List;

import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.Branch;
import md.fusionworks.lifehack.model.Currency;
import md.fusionworks.lifehack.model.Rate;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

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
