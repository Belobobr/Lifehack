package md.fusionworks.lifehack.data.repository;

import io.realm.Realm;
import io.realm.RealmResults;
import java.util.List;
import md.fusionworks.lifehack.data.persistence.SaleCategoryDataStore;
import md.fusionworks.lifehack.data.persistence.TaxiDataStore;
import md.fusionworks.lifehack.data.persistence.model.SaleCategory;
import md.fusionworks.lifehack.data.persistence.model.TaxiPhoneNumber;
import md.fusionworks.lifehack.data.persistence.model.mapper.SaleCategoryDataMapper;
import md.fusionworks.lifehack.data.persistence.model.mapper.TaxiPhoneNumberDataMapper;
import md.fusionworks.lifehack.ui.model.SaleCategoryModel;
import md.fusionworks.lifehack.ui.model.taxi.TaxiPhoneNumberModel;
import rx.Observable;

/**
 * Created by ungvas on 2/18/16.
 */
public class SaleCategoryRepository {

  private Realm realm;
  private SaleCategoryDataStore saleCategoryDataStore;
  private SaleCategoryDataMapper saleCategoryDataMapper;

  public SaleCategoryRepository(Realm realm, SaleCategoryDataStore saleCategoryDataStore) {
    this.realm = realm;
    this.saleCategoryDataStore = saleCategoryDataStore;
    saleCategoryDataMapper = new SaleCategoryDataMapper();
  }

  public boolean hasData() {
    RealmResults<SaleCategory> saleCategoryList =
        realm.where(SaleCategory.class).findAll();
    return saleCategoryList.size() > 0;
  }

  public void add(int[] saleCategoryIdList, String[] saleCategoryNameList) {
    saleCategoryDataStore.add(saleCategoryIdList,saleCategoryNameList);
  }

  public Observable<List<SaleCategoryModel>> getAllSaleCategories() {
    return realm.where(SaleCategory.class)
        .findAllAsync()
        .asObservable()
        .filter(saleCategories -> saleCategories.isLoaded())
        .first()
        .map(saleCategoryDataMapper::transform);
  }
}
