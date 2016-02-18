package md.fusionworks.lifehack.ui.event;

import md.fusionworks.lifehack.ui.model.taxi.TaxiPhoneNumberModel;

/**
 * Created by ungvas on 2/18/16.
 */
public class TaxiPhoneNumberClickEvent {

  private TaxiPhoneNumberModel taxiPhoneNumberModel;

  public TaxiPhoneNumberClickEvent(TaxiPhoneNumberModel taxiPhoneNumberModel) {
    this.taxiPhoneNumberModel = taxiPhoneNumberModel;
  }

  public TaxiPhoneNumberModel getTaxiPhoneNumberModel() {
    return taxiPhoneNumberModel;
  }

  public void setTaxiPhoneNumberModel(TaxiPhoneNumberModel taxiPhoneNumberModel) {
    this.taxiPhoneNumberModel = taxiPhoneNumberModel;
  }
}
