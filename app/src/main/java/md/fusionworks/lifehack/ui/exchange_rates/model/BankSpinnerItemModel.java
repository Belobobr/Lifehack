package md.fusionworks.lifehack.ui.exchange_rates.model;

/**
 * Created by ungvas on 10/30/15.
 */
public class BankSpinnerItemModel {

  private boolean isHeader;
  private String title;
  private int bankId;

  public BankSpinnerItemModel(boolean isHeader, String title, int bankId) {
    this.isHeader = isHeader;
    this.title = title;
    this.bankId = bankId;
  }

  public boolean isHeader() {
    return isHeader;
  }

  public void setIsHeader(boolean isHeader) {
    this.isHeader = isHeader;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getBankId() {
    return bankId;
  }

  public void setBankId(int bankId) {
    this.bankId = bankId;
  }
}
