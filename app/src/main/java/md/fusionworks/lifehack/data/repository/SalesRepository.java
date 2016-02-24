package md.fusionworks.lifehack.data.repository;

import java.util.List;
import md.fusionworks.lifehack.data.api.PricesService;
import md.fusionworks.lifehack.data.api.ServiceFactory;
import md.fusionworks.lifehack.data.api.model.sales.ProductDataMapper;
import md.fusionworks.lifehack.data.api.model.sales.SaleCategoryDataMapper;
import md.fusionworks.lifehack.ui.sales.model.ProductModel;
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import rx.Observable;

/**
 * Created by ungvas on 2/10/16.
 */
public class SalesRepository {

  private PricesService pricesService;
  private SaleCategoryDataMapper saleCategoryDataMapper;
  private ProductDataMapper productDataMapper;

  public SalesRepository() {
    pricesService = ServiceFactory.buildPricesService();
    saleCategoryDataMapper = new SaleCategoryDataMapper();
    productDataMapper = new ProductDataMapper();
  }

  public Observable<List<SaleCategoryModel>> getCategories() {
    return pricesService.getCategories()
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(categories -> saleCategoryDataMapper.transform(categories));
  }

  public Observable<List<ProductModel>> getProducts() {
    return pricesService.getProducts("2016-02-24", 1, 100, "ru", "ASC", 30, 0, 94, "zEjoa61qZS")
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(products -> productDataMapper.transform(products));
  }
}

