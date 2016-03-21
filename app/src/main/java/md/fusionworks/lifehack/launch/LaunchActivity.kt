package md.fusionworks.lifehack.launch

import android.os.Bundle
import md.fusionworks.lifehack.navigator.Navigator
import md.fusionworks.lifehack.taxi.TaxiRepository
import md.fusionworks.lifehack.taxi.persistence.TaxiData
import md.fusionworks.lifehack.view.activity.BaseActivity
import javax.inject.Inject

class LaunchActivity : BaseActivity() {

  @Inject lateinit var taxiRepository: TaxiRepository
  @Inject lateinit var appNavigator: Navigator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)

    saveTaxiPhoneNumbers()

    appNavigator.navigateToMainActivity(this)
    finish()
  }

  private fun saveTaxiPhoneNumbers() {
    if (!taxiRepository.hasData) taxiRepository.add(TaxiData.TAXI_PHONE_NUMBER_LIST)
  }
}