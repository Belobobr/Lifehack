package md.fusionworks.lifehack.taxi

import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.taxi.persistence.TaxiPhoneNumber
import javax.inject.Inject

/**
 * Created by ungvas on 2/18/16.
 */
@PerActivity
class TaxiPhoneNumberDataMapper @Inject constructor() {

  fun transform(taxiPhoneNumber: TaxiPhoneNumber) = TaxiPhoneNumberModel(
      taxiPhoneNumber.phoneNumber,
      taxiPhoneNumber.lastCallDate)

  fun transform(taxiPhoneNumberList: List<TaxiPhoneNumber>) = taxiPhoneNumberList.map {
    transform(it)
  }
}
