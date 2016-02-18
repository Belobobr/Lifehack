package md.fusionworks.lifehack.ui.model.taxi;

import java.util.Date;

/**
 * Created by ungvas on 2/18/16.
 */
public class TaxiPhoneNumberModel {

  private int phoneNumber;
  private Date lastCallDate;

  public TaxiPhoneNumberModel(int phoneNumber, Date lastCallDate) {
    this.phoneNumber = phoneNumber;
    this.lastCallDate = lastCallDate;
  }

  public int getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(int phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Date getLastCallDate() {
    return lastCallDate;
  }

  public void setLastCallDate(Date lastCallDate) {
    this.lastCallDate = lastCallDate;
  }
}
