package md.fusionworks.lifehack.util;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.lifehack.entity.Rate;

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

    public static double getCurrencyRateValue(List<Rate> rateList, int currencyId) {

        for (Rate rate : rateList) {

            if (rate.getCurrency().getId() == currencyId)
                return rate.getRateIn();
        }

        return 0;
    }

    public static double convert(double amountInValue, double currencyInValue, double currencyOutValue) {

        if (amountInValue == 0 || currencyInValue == 0 || currencyOutValue == 0)
            return 0;

        return amountInValue * currencyInValue / currencyOutValue;
    }
}
