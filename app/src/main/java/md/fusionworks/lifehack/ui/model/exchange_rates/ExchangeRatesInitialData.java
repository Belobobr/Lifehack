package md.fusionworks.lifehack.ui.model.exchange_rates;

import java.util.List;

/**
 * Created by ungvas on 2/10/16.
 */
public class ExchangeRatesInitialData {

    private List<BankModel> bankModelList;
    private List<CurrencyModel> currencyModelList;
    private List<RateModel> rateModelList;

    public ExchangeRatesInitialData(List<BankModel> bankModelList, List<CurrencyModel> currencyModelList, List<RateModel> rateModelList) {
        this.bankModelList = bankModelList;
        this.currencyModelList = currencyModelList;
        this.rateModelList = rateModelList;
    }

    public List<BankModel> getBankModelList() {
        return bankModelList;
    }

    public List<CurrencyModel> getCurrencyModelList() {
        return currencyModelList;
    }

    public List<RateModel> getRateModelList() {
        return rateModelList;
    }
}
