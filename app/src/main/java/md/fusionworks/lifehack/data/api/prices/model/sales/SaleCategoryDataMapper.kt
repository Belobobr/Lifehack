package md.fusionworks.lifehack.data.api.prices.model.sales

import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel

/**
 * Created by ungvas on 2/18/16.
 */
class SaleCategoryDataMapper {

  fun transform(saleCategory: SaleCategory) = SaleCategoryModel(saleCategory.id,
      saleCategory.nameRu, saleCategory.nameRo)

  fun transform(saleCategoryList: List<SaleCategory>) = saleCategoryList.map { transform(it) }
}
