package md.fusionworks.lifehack.sales.mapper

import md.fusionworks.lifehack.api.prices.model.sales.SaleCategory
import md.fusionworks.lifehack.sales.model.SaleCategoryModel

/**
 * Created by ungvas on 2/18/16.
 */
class SaleCategoryDataMapper {

  fun transform(saleCategory: SaleCategory) = SaleCategoryModel(saleCategory.id,
      saleCategory.nameRu, saleCategory.nameRo)

  fun transform(saleCategoryList: List<SaleCategory>) = saleCategoryList.map { transform(it) }
}
