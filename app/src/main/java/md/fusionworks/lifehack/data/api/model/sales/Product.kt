package md.fusionworks.lifehack.data.api.model.sales

/**
 * Created by ungvas on 2/24/16.
 */
class Product {
  var id: Long = 0
  var productMinPriceForGraph: Double = 0.toDouble()
  var productPrevPriceForGraph: Double = 0.toDouble()
  var categoryId: Int = 0
  var productName: String = ""
  var productImage: String = ""
  var storeProductId: Long = 0
  var percent: Double = 0.toDouble()
  var jbUrl: String = ""
  var categoryNameRu: String = ""
  var categoryNameRo: String = ""
  var storeProductUrl: String = ""
}
