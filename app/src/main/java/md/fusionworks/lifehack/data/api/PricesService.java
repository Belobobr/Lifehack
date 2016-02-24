package md.fusionworks.lifehack.data.api;

import java.util.List;
import md.fusionworks.lifehack.data.api.model.sales.Product;
import md.fusionworks.lifehack.data.api.model.sales.SaleCategory;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ungvas on 10/28/15.
 */
public interface PricesService {

  String ENDPOINT = "http://prices.digest.md/";

  String CATEGORIES = "api/categories?_format=json";
  String PRODUCTS = "api/result?_format=json";

  @GET(CATEGORIES) Observable<Response<List<SaleCategory>>> getCategories();

  @GET(PRODUCTS) Observable<Response<List<Product>>> getProducts(@Query("dateTo") String dateTo,
      @Query("minRange") int minRange, @Query("maxRange") int maxRange, @Query("lang") String lang,
      @Query("sort") String sort, @Query("limit") int limit, @Query("offset") int offset,
      @Query("categories") int categoryId, @Query("apiKey") String apiKey);
}
