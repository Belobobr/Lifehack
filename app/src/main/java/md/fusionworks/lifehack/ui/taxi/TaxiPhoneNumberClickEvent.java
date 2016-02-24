package md.fusionworks.lifehack.ui.taxi;

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
