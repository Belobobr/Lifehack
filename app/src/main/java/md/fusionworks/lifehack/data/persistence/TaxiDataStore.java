package md.fusionworks.lifehack.data.persistence;

import io.realm.Realm;
import java.util.Date;
import md.fusionworks.lifehack.data.persistence.model.TaxiPhoneNumber;

/**
 * Created by ungvas on 2/18/16.
 */
public class TaxiDataStore {

  private Realm realm;

  public TaxiDataStore(Realm realm) {
    this.realm = realm;
  }

  public void add(int[] phoneNumberList) {
    realm.executeTransaction(realm -> {
      for (int i = 0; i < phoneNumberList.length; i++) {
        TaxiPhoneNumber taxiPhoneNumber = realm.createObject(TaxiPhoneNumber.class);
        taxiPhoneNumber.setPhoneNumber(phoneNumberList[i]);
        taxiPhoneNumber.setLastCallDate(null);
      }
    });
  }

  public void updateLastUsedDate(int phoneNumber) {
    realm.executeTransaction(realm -> {
      TaxiPhoneNumber taxiPhoneNumber =
          realm.where(TaxiPhoneNumber.class).equalTo("phoneNumber", phoneNumber).findFirst();
      if (taxiPhoneNumber.isLoaded()) taxiPhoneNumber.setLastCallDate(new Date());
    });
  }
}
