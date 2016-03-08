package md.fusionworks.lifehack.data.repository

import java.util.HashMap
import md.fusionworks.lifehack.data.api.PricesService
import md.fusionworks.lifehack.data.api.ServiceFactory
import md.fusionworks.lifehack.data.api.model.sales.ProductDataMapper
import md.fusionworks.lifehack.data.api.model.sales.SaleCategoryDataMapper
import md.fusionworks.lifehack.ui.sales.model.ProductModel
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel
import md.fusionworks.lifehack.util.rx.ObservableTransformation
import rx.Observable

/**
 * Created by ungvas on 2/10/16.
 */
class SalesRepository {

  private val pricesService: PricesService
  private val saleCategoryDataMapper: SaleCategoryDataMapper
  private val productDataMapper: ProductDataMapper

  init {
    pricesService = ServiceFactory().buildPricesService()
    saleCategoryDataMapper = SaleCategoryDataMapper()
    productDataMapper = ProductDataMapper()
  }

  val saleCategories: Observable<List<SaleCategoryModel>>
    get() = pricesService.saleCategories().compose(
        ObservableTransformation.applyApiRequestConfiguration())
        .map({ saleCategoryDataMapper.transform(it) })

  fun getSaleProducts(dateTo: String, minRange: Int, maxRange: Int,
      lang: String, sort: String, limit: Int, offset: Int, categoryId: Int,
      apiKey: String): Observable<List<ProductModel>> {
    val params = HashMap<String, String>()
    params.put("dateTo", dateTo)
    params.put("minRange", minRange.toString())
    params.put("maxRange", maxRange.toString())
    params.put("lang", lang)
    params.put("sort", sort)
    params.put("limit", limit.toString())
    params.put("offset", offset.toString())
    if (categoryId != 0) params.put("categories[]", categoryId.toString())
    params.put("apikey", apiKey)

    return pricesService.getSaleProducts(params).compose(
        ObservableTransformation.applyApiRequestConfiguration())
        .map { productDataMapper.transform(it) }
  }
}

