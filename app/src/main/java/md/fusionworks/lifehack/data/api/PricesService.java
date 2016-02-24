package md.fusionworks.lifehack.data.api;

import java.util.List;
import md.fusionworks.lifehack.data.api.model.exchange_rates.Bank;
import md.fusionworks.lifehack.data.api.model.exchange_rates.Branch;
import md.fusionworks.lifehack.data.api.model.exchange_rates.Currency;
import md.fusionworks.lifehack.data.api.model.exchange_rates.Rate;
import md.fusionworks.lifehack.data.api.model.sales.SaleCategory;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ungvas on 10/28/15.
 */
public interface PricesService {

  String ENDPOINT = "http://prices.digest.md/";

  String CATEGORIES = "api/categories?_format=json";

  @GET(CATEGORIES) Observable<Response<List<SaleCategory>>> getCategories();
}
