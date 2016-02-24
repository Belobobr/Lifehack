package md.fusionworks.lifehack.data.repository;

import io.realm.Realm;
import io.realm.RealmResults;
import java.util.List;
import md.fusionworks.lifehack.data.persistence.taxi.TaxiDataStore;
import md.fusionworks.lifehack.data.persistence.taxi.TaxiPhoneNumber;
import md.fusionworks.lifehack.data.persistence.taxi.TaxiPhoneNumberDataMapper;
import md.fusionworks.lifehack.ui.taxi.TaxiPhoneNumberModel;
import rx.Observable;

/**
 * Created by ungvas on 2/18/16.
 */
public class TaxiRepository {

  private Realm realm;
  private TaxiDataStore taxiDataStore;
  private TaxiPhoneNumberDataMapper taxiPhoneNumberDataMapper;

  public TaxiRepository(Realm realm, TaxiDataStore taxiDataStore) {
    this.realm = realm;
    this.taxiDataStore = taxiDataStore;
    taxiPhoneNumberDataMapper = new TaxiPhoneNumberDataMapper();
  }

  public boolean hasData() {
    RealmResults<TaxiPhoneNumber> taxiPhoneNumberList =
        realm.where(TaxiPhoneNumber.class).findAll();
    return taxiPhoneNumberList.size() > 0;
  }

  public void add(int[] phoneNumberList) {
    taxiDataStore.add(phoneNumberList);
  }

  public Observable<List<TaxiPhoneNumberModel>> getAllPhoneNumbers() {
    return realm.where(TaxiPhoneNumber.class)
        .findAllAsync()
        .asObservable()
        .filter(taxiPhoneNumbers -> taxiPhoneNumbers.isLoaded())
        .first()
        .map(taxiPhoneNumberDataMapper::transform);
  }

  public void updateLastUsedDate(int phoneNumber) {
    taxiDataStore.updateLastUsedDate(phoneNumber);
  }
}
