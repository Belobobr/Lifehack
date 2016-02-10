package md.fusionworks.lifehack.data.api;

import java.util.List;

import md.fusionworks.lifehack.data.api.model.Bank;
import md.fusionworks.lifehack.data.api.model.Branch;
import md.fusionworks.lifehack.data.api.model.Currency;
import md.fusionworks.lifehack.data.api.model.Rate;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ungvas on 10/28/15.
 */
public interface LifehackService {

    String ENDPOINT = "http://banks.digest.md/";

    String BANKS = "api/banks.json?_format=json";
    String RATES = "api/rates.json?_format=json";
    String CURRENCIES = "api/currencies.json?_format=json";
    String BRANCHES = "api/banks/{bank}/branches.json?_format=json";

    @GET(BANKS)
    Observable<Response<List<Bank>>> getBanks();

    @GET(CURRENCIES)
    Observable<Response<List<Currency>>> getCurrencies();

    @GET(RATES)
    Observable<Response<List<Rate>>> getRates(@Query("date") String date);

    @GET(BRANCHES)
    Observable<Response<List<Branch>>> getBankBranches(@Path("bank") int bank, @Query("active") boolean active);
}
