package md.fusionworks.lifehack.ui.exchange_rates.model;

/**
 * Created by ungvas on 10/30/15.
 */
public class BestExchangeModel {

  private BankModel bank;
  private double amountOutvalue;

  public BestExchangeModel() {
  }

  public BestExchangeModel(BankModel bank, double amountOutvalue) {
    this.bank = bank;
    this.amountOutvalue = amountOutvalue;
  }

  public BankModel getBank() {
    return bank;
  }

  public void setBank(BankModel bank) {
    this.bank = bank;
  }

  public double getAmountOutvalue() {
    return amountOutvalue;
  }

  public void setAmountOutvalue(double amountOutvalue) {
    this.amountOutvalue = amountOutvalue;
  }
}
