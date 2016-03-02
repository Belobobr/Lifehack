package md.fusionworks.lifehack.ui;

import android.os.Bundle;
import io.realm.Realm;
import md.fusionworks.lifehack.data.persistence.taxi.TaxiData;
import md.fusionworks.lifehack.data.persistence.taxi.TaxiDataStore;
import md.fusionworks.lifehack.data.repository.TaxiRepository;
import md.fusionworks.lifehack.ui.base.view.BaseActivity;

public class LaunchActivity extends BaseActivity {

  private Realm realm;
  private TaxiRepository taxiRepository;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    realm = Realm.getDefaultInstance();
    taxiRepository = new TaxiRepository(realm, new TaxiDataStore(realm));
    saveTaxiPhoneNumbers();

    getNavigator().navigateToMainActivity(this);
    finish();
  }

  private void saveTaxiPhoneNumbers() {
    if (!taxiRepository.hasData()) taxiRepository.add(TaxiData.TAXI_PHONE_NUMBER_LIST);
  }
}