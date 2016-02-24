package md.fusionworks.lifehack.data.repository;

import java.util.List;
import md.fusionworks.lifehack.data.api.PricesService;
import md.fusionworks.lifehack.data.api.ServiceFactory;
import md.fusionworks.lifehack.data.api.model.sales.SaleCategoryDataMapper;
import md.fusionworks.lifehack.ui.sales.SaleCategoryModel;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import rx.Observable;

/**
 * Created by ungvas on 2/10/16.
 */
public class SalesRepository {

  private PricesService pricesService;
  private SaleCategoryDataMapper saleCategoryDataMapper;

  public SalesRepository() {
    pricesService = ServiceFactory.buildPricesService();
    saleCategoryDataMapper = new SaleCategoryDataMapper();
  }

  public Observable<List<SaleCategoryModel>> getCategories() {
    return pricesService.getCategories()
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(categories -> saleCategoryDataMapper.transform(categories));
  }
}

