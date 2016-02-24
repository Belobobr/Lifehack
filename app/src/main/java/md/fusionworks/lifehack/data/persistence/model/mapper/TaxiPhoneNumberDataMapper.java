package md.fusionworks.lifehack.data.persistence.model.mapper;

import java.util.ArrayList;
import java.util.List;
import md.fusionworks.lifehack.data.persistence.model.TaxiPhoneNumber;
import md.fusionworks.lifehack.ui.model.taxi.TaxiPhoneNumberModel;

/**
 * Created by ungvas on 2/18/16.
 */
public class TaxiPhoneNumberDataMapper {

  public TaxiPhoneNumberModel transform(TaxiPhoneNumber taxiPhoneNumber) {
    return new TaxiPhoneNumberModel(taxiPhoneNumber.getPhoneNumber(),
        taxiPhoneNumber.getLastCallDate());
  }

  public List<TaxiPhoneNumberModel> transform(List<TaxiPhoneNumber> taxiPhoneNumberList) {
    List<TaxiPhoneNumberModel> taxiPhoneNumberModelList =
        new ArrayList<>(taxiPhoneNumberList.size());
    TaxiPhoneNumberModel taxiPhoneNumberModel;
    for (TaxiPhoneNumber taxiPhoneNumber : taxiPhoneNumberList) {
      taxiPhoneNumberModel = transform(taxiPhoneNumber);
      if (taxiPhoneNumberModel != null) {
        taxiPhoneNumberModelList.add(taxiPhoneNumberModel);
      }
    }
    return taxiPhoneNumberModelList;
  }
}
