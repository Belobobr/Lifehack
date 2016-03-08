package md.fusionworks.lifehack.data.api.model.sales

import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel

/**
 * Created by ungvas on 2/18/16.
 */
class SaleCategoryDataMapper {

  fun transform(saleCategory: SaleCategory): SaleCategoryModel {
    return SaleCategoryModel(saleCategory.id, saleCategory.nameRu, saleCategory.nameRo)
  }

  fun transform(saleCategoryList: List<SaleCategory>): List<SaleCategoryModel> {
    val saleCategoryModelList = arrayListOf<SaleCategoryModel>();
    var saleCategoryModel: SaleCategoryModel?
    for (saleCategory in saleCategoryList) {
      saleCategoryModel = transform(saleCategory)
      if (saleCategoryModel != null) {
        saleCategoryModelList.add(saleCategoryModel)
      }
    }
    return saleCategoryModelList
  }
}
