package md.fusionworks.lifehack.data.persistence.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by ungvas on 2/18/16.
 */
public class TaxiPhoneNumber extends RealmObject {

  @PrimaryKey private int phoneNumber;
  private Date lastCallDate;

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
