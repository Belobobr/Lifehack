package md.fusionworks.lifehack.ui.model.exchange_rates;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ungvas on 10/28/15.
 */
public class RateModel {

  private int id;
  @SerializedName("rate_in") private double rateIn;
  @SerializedName("rate_out") private double rateOut;
  private Date date;
  private CurrencyModel currency;
  private BankModel bank;

  public RateModel(int id, double rateIn, double rateOut, Date date, CurrencyModel currency,
      BankModel bank) {
    this.id = id;
    this.rateIn = rateIn;
    this.rateOut = rateOut;
    this.date = date;
    this.currency = currency;
    this.bank = bank;
  }

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

  public CurrencyModel getCurrency() {
    return currency;
  }

  public void setCurrency(CurrencyModel currency) {
    this.currency = currency;
  }

  public BankModel getBank() {
    return bank;
  }

  public void setBank(BankModel bank) {
    this.bank = bank;
  }
}
