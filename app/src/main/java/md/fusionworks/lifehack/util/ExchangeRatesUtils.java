package md.fusionworks.lifehack.util;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.lifehack.ui.model.exchange_rates.RateModel;

/**
 * Created by ungvas on 10/30/15.
 */
public class ExchangeRatesUtils {

  public static List<RateModel> getBankRates(List<RateModel> rateList, int bankId) {

    List<RateModel> bankRateList = new ArrayList<>();
    for (RateModel rate : rateList) {

      if (rate.getBank().getId() == bankId) bankRateList.add(rate);
    }

    return bankRateList;
  }

  public static double getCurrencyRateValue(List<RateModel> rateList, int currencyId) {

    for (RateModel rate : rateList) {

      if (rate.getCurrency().getId() == currencyId) return rate.getRateIn();
    }

    return 0;
  }

  public static double convert(double amountInValue, double currencyInValue,
      double currencyOutValue) {

    if (amountInValue == 0 || currencyInValue == 0 || currencyOutValue == 0) return 0;

    return amountInValue * currencyInValue / currencyOutValue;
  }
}
