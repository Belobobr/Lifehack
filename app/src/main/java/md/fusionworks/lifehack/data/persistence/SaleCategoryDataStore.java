package md.fusionworks.lifehack.data.persistence;

import io.realm.Realm;
import java.util.Date;
import md.fusionworks.lifehack.data.persistence.model.SaleCategory;
import md.fusionworks.lifehack.data.persistence.model.TaxiPhoneNumber;

/**
 * Created by ungvas on 2/18/16.
 */
public class SaleCategoryDataStore {

  private Realm realm;

  public SaleCategoryDataStore(Realm realm) {
    this.realm = realm;
  }

  public void add(int[] saleCategoryIdList, String[] saleCategoryNameList) {
    realm.executeTransaction(realm -> {
      for (int i = 0; i < saleCategoryIdList.length; i++) {
        SaleCategory saleCategory = realm.createObject(SaleCategory.class);
        saleCategory.setId(saleCategoryIdList[i]);
        saleCategory.setName(saleCategoryNameList[i]);
      }
    });
  }
}
