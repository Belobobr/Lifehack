package md.fusionworks.lifehack.data.api;

import java.util.List;
import java.util.Map;
import md.fusionworks.lifehack.data.api.model.sales.Product;
import md.fusionworks.lifehack.data.api.model.sales.SaleCategory;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by ungvas on 10/28/15.
 */
public interface PricesService {

  String ENDPOINT = "http://prices.digest.md/";

  String SALE_CATEGORIES = "api/categories?_format=json";
  String SALE_PRODUCTS = "api/result?_format=json";

  @GET(SALE_CATEGORIES) Observable<Response<List<SaleCategory>>> getSaleCategories();

  @GET(SALE_PRODUCTS) Observable<Response<List<Product>>> getSaleProducts(
      @QueryMap Map<String, String> params);
}
