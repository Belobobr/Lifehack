package md.fusionworks.lifehack.entity;

/**
 * Created by ungvas on 10/30/15.
 */
public class BestExchange {

    private Bank bank;
    private double amountOutvalue;

    public BestExchange() {
    }

    public BestExchange(Bank bank, double amountOutvalue) {
        this.bank = bank;
        this.amountOutvalue = amountOutvalue;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public double getAmountOutvalue() {
        return amountOutvalue;
    }

    public void setAmountOutvalue(double amountOutvalue) {
        this.amountOutvalue = amountOutvalue;
    }
}
