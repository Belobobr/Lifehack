package md.fusionworks.lifehack.data.api.prices

import md.fusionworks.lifehack.data.api.prices.model.sales.Product
import md.fusionworks.lifehack.data.api.prices.model.sales.SaleCategory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by ungvas on 10/28/15.
 */
interface PricesService {

  @GET(SALE_CATEGORIES) fun saleCategories(): Observable<Response<List<SaleCategory>>>

  @GET(SALE_PRODUCTS) fun getSaleProducts(
      @QueryMap params: Map<String, String>): Observable<Response<List<Product>>>

  companion object {
    const val ENDPOINT = "http://prices.digest.md/"
    const val SALE_CATEGORIES = "api/categories?_format=json"
    const val SALE_PRODUCTS = "api/result?_format=json"
  }
}
