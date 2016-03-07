package md.fusionworks.lifehack.ui.sales.model

/**
 * Created by ungvas on 2/24/16.
 */
class ProductModel(var id: Long, var productMinPriceForGraph: Double, var productPrevPriceForGraph: Double,
    var categoryId: Int, var productName: String, var productImage: String, var storeProductId: Long, var percent: Double,
    var jbUrl: String, var categoryNameRu: String, var categoryNameRo: String, var storeProductUrl: String)
