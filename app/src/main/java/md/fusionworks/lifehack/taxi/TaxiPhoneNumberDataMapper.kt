package md.fusionworks.lifehack.taxi

import md.fusionworks.lifehack.taxi.persistence.TaxiPhoneNumber
import md.fusionworks.lifehack.taxi.TaxiPhoneNumberModel

/**
 * Created by ungvas on 2/18/16.
 */
class TaxiPhoneNumberDataMapper {

  fun transform(taxiPhoneNumber: TaxiPhoneNumber): TaxiPhoneNumberModel = TaxiPhoneNumberModel(
      taxiPhoneNumber.phoneNumber,
      taxiPhoneNumber.lastCallDate)

  fun transform(taxiPhoneNumberList: List<TaxiPhoneNumber>) = taxiPhoneNumberList.map {
    transform(it)
  }
}
