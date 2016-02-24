package md.fusionworks.lifehack.data.api.model.sales;

import java.util.ArrayList;
import java.util.List;
import md.fusionworks.lifehack.ui.sales.model.ProductModel;
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel;

/**
 * Created by ungvas on 2/18/16.
 */
public class ProductDataMapper {

  public ProductModel transform(Product product) {
    return new ProductModel(product.id, product.productMinPriceForGraph,
        product.productPrevPriceForGraph, product.categoryId, product.productName,
        product.productImage, product.storeProductId, product.percent, product.jbUrl,
        product.categoryNameRu, product.categoryNameRo, product.storeProductUrl);
  }

  public List<ProductModel> transform(List<Product> productList) {
    List<ProductModel> productModelList = new ArrayList<>(productList.size());
    ProductModel productModel;
    for (Product product : productList) {
      productModel = transform(product);
      if (productModel != null) {
        productModelList.add(productModel);
      }
    }
    return productModelList;
  }
}
