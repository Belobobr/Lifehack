package md.fusionworks.lifehack.ui.sales.model;

/**
 * Created by ungvas on 2/24/16.
 */
public class ProductModel {
  public long id;
  public double productMinPriceForGraph;
  public double productPrevPriceForGraph;
  public int categoryId;
  public String productName;
  public String productImage;
  public long storeProductId;
  public double percent;
  public String jbUrl;
  public String categoryNameRu;
  public String categoryNameRo;
  public String storeProductUrl;

  public ProductModel(long id, double productMinPriceForGraph, double productPrevPriceForGraph,
      int categoryId, String productName, String productImage, long storeProductId, double percent,
      String jbUrl, String categoryNameRu, String categoryNameRo, String storeProductUrl) {
    this.id = id;
    this.productMinPriceForGraph = productMinPriceForGraph;
    this.productPrevPriceForGraph = productPrevPriceForGraph;
    this.categoryId = categoryId;
    this.productName = productName;
    this.productImage = productImage;
    this.storeProductId = storeProductId;
    this.percent = percent;
    this.jbUrl = jbUrl;
    this.categoryNameRu = categoryNameRu;
    this.categoryNameRo = categoryNameRo;
    this.storeProductUrl = storeProductUrl;
  }
}
