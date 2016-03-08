package md.fusionworks.lifehack.data.persistence.taxi

import md.fusionworks.lifehack.ui.taxi.TaxiPhoneNumberModel

/**
 * Created by ungvas on 2/18/16.
 */
class TaxiPhoneNumberDataMapper {

  fun transform(taxiPhoneNumber: TaxiPhoneNumber): TaxiPhoneNumberModel {
    return TaxiPhoneNumberModel(taxiPhoneNumber.phoneNumber,
        taxiPhoneNumber.lastCallDate)
  }

  fun transform(taxiPhoneNumberList: List<TaxiPhoneNumber>): List<TaxiPhoneNumberModel> {
    val taxiPhoneNumberModelList = arrayListOf<TaxiPhoneNumberModel>()
    var taxiPhoneNumberModel: TaxiPhoneNumberModel?
    for (taxiPhoneNumber in taxiPhoneNumberList) {
      taxiPhoneNumberModel = transform(taxiPhoneNumber)
      if (taxiPhoneNumberModel != null) {
        taxiPhoneNumberModelList.add(taxiPhoneNumberModel)
      }
    }
    return taxiPhoneNumberModelList
  }
}
