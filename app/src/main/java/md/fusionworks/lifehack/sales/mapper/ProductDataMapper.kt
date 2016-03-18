package md.fusionworks.lifehack.sales.mapper

import md.fusionworks.lifehack.api.prices.model.sales.Product
import md.fusionworks.lifehack.sales.model.ProductModel

/**
 * Created by ungvas on 2/18/16.
 */
class ProductDataMapper {

  fun transform(product: Product) = ProductModel(product.id,
      product.productMinPriceForGraph,
      product.productPrevPriceForGraph, product.categoryId, product.productName,
      product.productImage, product.storeProductId, product.percent,
      product.jbUrl, product.categoryNameRu, product.categoryNameRo,
      product.storeProductUrl)

  fun transform(productList: List<Product>) = productList.map { transform(it) }
}
