package md.fusionworks.lifehack.data.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  public Observable<List<SaleCategoryModel>> getSaleCategories() {
    return pricesService.getSaleCategories()
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(saleCategories -> saleCategoryDataMapper.transform(saleCategories));
  }

  public Observable<List<ProductModel>> getSaleProducts(String dateTo, int minRange, int maxRange,
      String lang, String sort, int limit, int offset, int categoryId, String apiKey) {
    Map<String, String> params = new HashMap<>();
    params.put("dateTo", dateTo);
    params.put("minRange", String.valueOf(minRange));
    params.put("maxRange", String.valueOf(maxRange));
    params.put("lang", lang);
    params.put("sort", sort);
    params.put("limit", String.valueOf(limit));
    params.put("offset", String.valueOf(offset));
    if (categoryId != 0) params.put("categories[]", String.valueOf(categoryId));
    params.put("apikey", apiKey);

    return pricesService.getSaleProducts(params)
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(products -> productDataMapper.transform(products));
  }
}

