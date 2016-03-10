package md.fusionworks.lifehack.ui

import android.os.Bundle
import io.realm.Realm
import md.fusionworks.lifehack.data.persistence.taxi.TaxiData
import md.fusionworks.lifehack.data.persistence.taxi.TaxiDataStore
import md.fusionworks.lifehack.data.repository.TaxiRepository
import md.fusionworks.lifehack.ui.base.view.BaseActivity

class LaunchActivity : BaseActivity() {

  private lateinit var realm: Realm
  private lateinit var taxiRepository: TaxiRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    realm = Realm.getDefaultInstance()
    taxiRepository = TaxiRepository(realm, TaxiDataStore(realm))
    saveTaxiPhoneNumbers()

    navigator.navigateToMainActivity(this)
    finish()
  }

  private fun saveTaxiPhoneNumbers() {
    if (!taxiRepository.hasData) taxiRepository.add(TaxiData.TAXI_PHONE_NUMBER_LIST)
  }
}