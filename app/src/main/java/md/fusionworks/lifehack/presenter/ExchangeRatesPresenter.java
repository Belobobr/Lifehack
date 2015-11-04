package md.fusionworks.lifehack.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import md.fusionworks.lifehack.data.net.LifehackClient;
import md.fusionworks.lifehack.data.net.ServiceCreator;
import md.fusionworks.lifehack.data.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.BestExchange;
import md.fusionworks.lifehack.model.Currency;
import md.fusionworks.lifehack.model.Rate;
import md.fusionworks.lifehack.ui.view.ExchangeRatesView;
import md.fusionworks.lifehack.util.Converter;
import md.fusionworks.lifehack.util.DateUtils;
import md.fusionworks.lifehack.util.ExchangeRatesUtils;

/**
 * Created by ungvas on 10/22/15.
 */
@PerActivity
public class ExchangeRatesPresenter implements Presenter<ExchangeRatesView> {

    private static final int DEFAULT_AMOUNT_IN_VALUE = 100;
    public static final int DEFAULT_CURRENCY_IN_ID = 2;
    public static final int DEFAULT_CURRENCY_OUT_ID = 1;
    public static final int DEFAULT_BANK_ID = 2;
    public static final String NO_EXCHANGE_RATES_OUT_VALUE = "-";

    private Context context;
    private ExchangeRatesView exchangeRatesView;

    private LifehackClient lifehackClient;
    private List<Rate> rateList;
    private List<Bank> bankList;
    private List<Currency> currencyList;

    @Inject ExchangeRatesRepository exchangeRatesRepository;

    @Inject
    public ExchangeRatesPresenter(Context context) {

        this.context = context;
        lifehackClient = ServiceCreator.createService(LifehackClient.class, LifehackClient.BASE_URL, "cb5fa2d6b00257fd769d2c68bf32c1a42ea0fd7c");
    }

    @Override
    public void attachView(@NonNull ExchangeRatesView view) {

        exchangeRatesView = view;
    }

    @Override
    public void detachView(@NonNull ExchangeRatesView view) {

        exchangeRatesView = null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public void initialize() {

        loadInitialData();
    }

    private void loadInitialData() {

        exchangeRatesView.showLoadingRates();
        loadBanks();
        loadCurrencies();
        loadTodayRates();
    }

    private boolean isInitialDataLoaded() {

        return (bankList != null && currencyList != null && rateList != null);
    }

    private void onInitialDataLoadedSuccess() {

        exchangeRatesView.hideLoadingRates();
        initializeUI();
    }

    private void onInitialDataLoadedError() {

        exchangeRatesView.hideLoadingRates();
        exchangeRatesView.showLoadingRatesError();
    }


    private void loadBanks() {

        exchangeRatesRepository.getBanks(new md.fusionworks.lifehack.data.net.Callback<List<Bank>>() {
            @Override
            public void onSuccess(List<Bank> response) {

                bankList = response;
                exchangeRatesView.populateBankSpinner(response);

                if (isInitialDataLoaded())
                    onInitialDataLoadedSuccess();
            }

            @Override
            public void onError(Throwable t) {

                onInitialDataLoadedError();
            }
        });
    }

    private void loadCurrencies() {

        exchangeRatesRepository.getCurrencies(new md.fusionworks.lifehack.data.net.Callback<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> response) {

                currencyList = response;
                exchangeRatesView.populateCurrencyInGroup(response);
                exchangeRatesView.populateCurrencyOutGroup(response);

                if (isInitialDataLoaded())
                    onInitialDataLoadedSuccess();
            }

            @Override
            public void onError(Throwable t) {

                exchangeRatesView.hideLoadingRates();
            }
        });
    }

    private void loadRates(String date, boolean initializeUI) {

        exchangeRatesRepository.getRates(date, new md.fusionworks.lifehack.data.net.Callback<List<Rate>>() {
            @Override
            public void onSuccess(List<Rate> response) {

                rateList = response;


                if (isInitialDataLoaded()) {

                    if (initializeUI)
                        onInitialDataLoadedSuccess();
                    else {

                        exchangeRatesView.hideLoadingRates();
                        applyConversion();
                    }
                }
            }

            @Override
            public void onError(Throwable t) {

                if (initializeUI)
                    exchangeRatesView.hideLoadingRates();
            }
        });
    }

    private void loadTodayRates() {

        String today = DateUtils.getRateDateFormat().format(new Date());
        loadRates(today, true);
    }

    private void initializeUI() {

        exchangeRatesView.setAmountInText(String.valueOf(DEFAULT_AMOUNT_IN_VALUE));
        exchangeRatesView.setCurrencyInCheckedById(DEFAULT_CURRENCY_IN_ID);
        exchangeRatesView.setCurrencyOutCheckedById(DEFAULT_CURRENCY_OUT_ID);
        exchangeRatesView.setBankSelection(DEFAULT_BANK_ID);
        exchangeRatesView.initializeViewListeners();
    }

    private void notifyNoExchangeRates() {

        exchangeRatesView.showNotificationToast("Нету курса",
                ExchangeRatesView.NotificationToastType.ERROR);
        exchangeRatesView.setAmountOutText(NO_EXCHANGE_RATES_OUT_VALUE);
    }

    private boolean validate(List<Rate> bankRateList, double currencyInRateValue, double currencyOutRateValue) {

        if (bankRateList.size() == 0 || currencyInRateValue == 0 || currencyOutRateValue == 0) {

            notifyNoExchangeRates();
            return false;
        }

        return true;
    }

    public void applyConversion() {

        List<Rate> bankRateList;
        double amountInValue;
        double currencyInRateValue;
        double currencyOutRateValue;

        int bankId = exchangeRatesView.getSelectedBankId();

        if (bankId == 0) {


        } else {

            bankRateList = ExchangeRatesUtils.getBankRates(rateList, exchangeRatesView.getSelectedBankId());
            amountInValue = Converter.toDouble(exchangeRatesView.getAmountInText());
            currencyInRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, exchangeRatesView.getCheckedCurrencyInId());
            currencyOutRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, exchangeRatesView.getCheckedCurrencyOutId());

            if (validate(bankRateList, currencyInRateValue, currencyOutRateValue)) {

                double amountOutValue = convertBank(bankRateList, amountInValue, currencyInRateValue, currencyOutRateValue);
                exchangeRatesView.setAmountOutText(String.format("%.2f", amountOutValue));
            }
        }

/*
        boolean bestExchangeOption = bankId == 0;
        if (bestExchangeOption) {

            BestExchange bestExchange = convertBestExchange(rateList, amountInValue, currencyInId, currencyOutId);
            exchangeRatesView.setAmountOutText(String.format("%.2f", bestExchange.getAmountOutvalue()));

            String bestExchangeBankText = (bestExchange.getBank() != null) ?
                    String.format("Используется курс банка %s", bestExchange.getBank().getName()) :
                    "Не найден подходящий банк";
            exchangeRatesView.setBestExchangeBankText(bestExchangeBankText);
        }*/
    }

    private double convertBank(List<Rate> rateList, double amountInValue, double currencyInRateValue,
                               double currencyOutRateValue) {

        if (amountInValue == 0)
            return 0;

        double amountOutValue = ExchangeRatesUtils.convert(amountInValue, currencyInRateValue, currencyOutRateValue);

        return amountOutValue;
    }

    /* private BestExchange convertBestExchange(List<Rate> rateList, double amountInValue, int currencyInId, int currencyOutId) {

           double amountOutValue = 0;
           BestExchange bestExchange = new BestExchange();
           for (Bank bank : bankList) {

               if (bank.getId() != 1) {
                   double bankAmountOutValue = convertBank(rateList, amountInValue, bank.getId(), currencyInId, currencyOutId);
                   if (bankAmountOutValue > amountOutValue) {

                       amountOutValue = bankAmountOutValue;
                       bestExchange = new BestExchange(bank, amountOutValue);
                   }
               }
           }

           return bestExchange;
       }
   */
    public void onRatesDateChanged(Date date) {

        exchangeRatesView.showLoadingRates();
        String dateText = DateUtils.getRateDateFormat().format(date);
        loadRates(dateText, false);
    }

    public void onCurrencyInChanged(RadioGroup radioGroup, int checkedId) {

        int currencyInId = checkedId;
        int currencyOutId = exchangeRatesView.getCheckedCurrencyOutId();

        if (currencyInId == currencyOutId) {

            exchangeRatesView.currencyOutCheckNext(checkedId);
        }

        applyConversion();
    }

    public void onCurrencyOutChanged(RadioGroup radioGroup, int checkedId) {

        int currencyOutId = checkedId;
        int currencyInId = exchangeRatesView.getCheckedCurrencyInId();

        if (currencyInId == currencyOutId) {

            exchangeRatesView.currencyInCheckNext(checkedId);
        }

        applyConversion();
    }

    public void afterAmountInTextChanged(String text) {

        applyConversion();
    }

    public void onBankSelected(int position, long id) {

        applyConversion();
    }
}
