package md.fusionworks.lifehack.util;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.BestExchange;
import md.fusionworks.lifehack.model.Rate;

/**
 * Created by ungvas on 10/30/15.
 */
public class ExchangeRatesUtils {

    public static List<Rate> getBankRates(List<Rate> rateList, int bankId) {

        List<Rate> bankRateList = new ArrayList<>();
        for (Rate rate : rateList) {

            if (rate.getBank().getId() == bankId)
                bankRateList.add(rate);
        }

        return bankRateList;
    }

    public static double getCurrencyRateList(List<Rate> rateList, int currencyId) {

        for (Rate rate : rateList) {

            if (rate.getCurrency().getId() == currencyId)
                return rate.getRateIn();
        }

        return 0;
    }

    public static double convert(double amountInValue, double currencyInValue, double currencyOutValue) {

        return amountInValue * currencyInValue / currencyOutValue;
    }
}