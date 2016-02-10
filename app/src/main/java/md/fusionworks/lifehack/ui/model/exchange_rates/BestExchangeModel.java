package md.fusionworks.lifehack.ui.model.exchange_rates;

import md.fusionworks.lifehack.ui.model.exchange_rates.BankModel;

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
