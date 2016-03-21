package md.fusionworks.lifehack.launch

import android.os.Bundle
import md.fusionworks.lifehack.di.HasComponent
import md.fusionworks.lifehack.taxi.TaxiRepository
import md.fusionworks.lifehack.taxi.persistence.TaxiData
import md.fusionworks.lifehack.view.activity.BaseActivity
import javax.inject.Inject

class LaunchActivity : BaseActivity(), HasComponent<LaunchComponent> {

  override lateinit var component: LaunchComponent

  @Inject lateinit var taxiRepository: TaxiRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    initializeDIComponent()
    super.onCreate(savedInstanceState)

    saveTaxiPhoneNumbers()

    navigator.navigateToMainActivity(this)
    finish()
  }

  private fun saveTaxiPhoneNumbers() {
    if (!taxiRepository.hasData) taxiRepository.add(TaxiData.TAXI_PHONE_NUMBER_LIST)
  }

  override fun initializeDIComponent() {
    component = DaggerLaunchComponent
        .builder()
        .appComponent(appComponent)
        .build()
    component.inject(this)
  }
}