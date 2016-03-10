package md.fusionworks.lifehack.data.persistence.taxi

import md.fusionworks.lifehack.ui.taxi.TaxiPhoneNumberModel

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
