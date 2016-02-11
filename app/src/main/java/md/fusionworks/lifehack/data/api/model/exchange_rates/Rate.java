package md.fusionworks.lifehack.data.api.model.exchange_rates;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ungvas on 10/28/15.
 */
public class Rate {

  private int id;
  @SerializedName("rate_in") private double rateIn;
  @SerializedName("rate_out") private double rateOut;
  private Date date;
  private Currency currency;
  private Bank bank;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getRateIn() {
    return rateIn;
  }

  public void setRateIn(double rateIn) {
    this.rateIn = rateIn;
  }

  public double getRateOut() {
    return rateOut;
  }

  public void setRateOut(double rateOut) {
    this.rateOut = rateOut;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }
}
