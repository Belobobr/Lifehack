package md.fusionworks.lifehack.data.api.model.mapper;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.lifehack.data.api.model.Bank;
import md.fusionworks.lifehack.data.api.model.Currency;
import md.fusionworks.lifehack.ui.model.BankModel;
import md.fusionworks.lifehack.ui.model.CurrencyModel;

/**
 * Created by ungvas on 2/10/16.
 */
public class CurrencyDataMapper {

    public CurrencyModel transform(Currency currency) {
        return new CurrencyModel(currency.getId(), currency.getName());
    }

    public List<CurrencyModel> transform(List<Currency> currencyList) {
        List<CurrencyModel> currencyModelList = new ArrayList<>(currencyList.size());
        CurrencyModel currencyModel;
        for (Currency currency : currencyList) {
            currencyModel = transform(currency);
            if (currencyModel != null) {
                currencyModelList.add(currencyModel);
            }
        }

        return currencyModelList;
    }
}
