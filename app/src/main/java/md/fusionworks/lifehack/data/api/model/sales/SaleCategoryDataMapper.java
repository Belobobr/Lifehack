package md.fusionworks.lifehack.data.api.model.sales;

import java.util.ArrayList;
import java.util.List;
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel;

/**
 * Created by ungvas on 2/18/16.
 */
public class SaleCategoryDataMapper {

  public SaleCategoryModel transform(SaleCategory saleCategory) {
    return new SaleCategoryModel(saleCategory.id, saleCategory.nameRu, saleCategory.nameRo);
  }

  public List<SaleCategoryModel> transform(List<SaleCategory> saleCategoryList) {
    List<SaleCategoryModel> saleCategoryModelList = new ArrayList<>(saleCategoryList.size());
    SaleCategoryModel saleCategoryModel;
    for (SaleCategory saleCategory : saleCategoryList) {
      saleCategoryModel = transform(saleCategory);
      if (saleCategoryModel != null) {
        saleCategoryModelList.add(saleCategoryModel);
      }
    }
    return saleCategoryModelList;
  }
}
