package md.fusionworks.lifehack.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.data.net.Callback;
import md.fusionworks.lifehack.data.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.interactor.GetBanksImpl;
import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.Branch;
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

    private List<Rate> rateList;
    private List<Bank> bankList;
    private List<Currency> currencyList;

    private ExchangeRatesRepository exchangeRatesRepository;
      @Inject GetBanksImpl getBanksUseCase;

    @Inject
    public ExchangeRatesPresenter(@Named("activity") Context context) {

        this.context = context;
        //getBanksUseCase = new GetBanksImpl(context);
        exchangeRatesRepository = new ExchangeRatesRepository(context);
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

        String text = context.getString(R.string.field_loading_rates_);
        exchangeRatesView.showLoading(text);
       loadBanksUseCase();
        loadCurrencies();
        loadTodayRates();
    }

    private boolean isInitialDataLoaded() {

        return (bankList != null && currencyList != null && rateList != null);
    }

    private void onInitialDataLoadedSuccess() {

        exchangeRatesView.hideRetryView();
        exchangeRatesView.showExchangeRatesView();
        exchangeRatesView.hideLoading();
        setupUIWithDefaultValues();
    }

    private void onInitialDataLoadedError() {

        exchangeRatesView.hideLoading();
        exchangeRatesView.hideExchangeRatesView();
        exchangeRatesView.showRetryView();
    }

    private void loadBanksUseCase() {

        getBanksUseCase.execute(new Callback<List<Bank>>() {
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

                onInitialDataLoadedError();
            }
        });
    }

    private void loadInitialRates(String date) {

        exchangeRatesRepository.getRates(date, new md.fusionworks.lifehack.data.net.Callback<List<Rate>>() {
            @Override
            public void onSuccess(List<Rate> response) {

                rateList = response;


                if (isInitialDataLoaded()) {
                    onInitialDataLoadedSuccess();
                }
            }

            @Override
            public void onError(Throwable t) {

                onInitialDataLoadedError();
            }
        });
    }

    private void loadRates(String date) {

        exchangeRatesRepository.getRates(date, new md.fusionworks.lifehack.data.net.Callback<List<Rate>>() {
            @Override
            public void onSuccess(List<Rate> response) {

                rateList = response;
                exchangeRatesView.hideLoading();
                applyConversion();
            }

            @Override
            public void onError(Throwable t) {

                exchangeRatesView.hideLoading();
                exchangeRatesView.showLoadingRatesError(date);
            }
        });
    }

    private void loadBankBranches(int bankId, boolean active) {

        exchangeRatesRepository.getBankBranches(bankId, active, new Callback<List<Branch>>() {
            @Override
            public void onSuccess(List<Branch> response) {

                exchangeRatesView.hideLoading();
            }

            @Override
            public void onError(Throwable t) {

                exchangeRatesView.hideLoading();
            }
        });
    }

    private void loadTodayRates() {

        String today = DateUtils.getRateDateFormat().format(new Date());
        loadInitialRates(today);
    }

    private void setupUIWithDefaultValues() {

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

    private boolean validateConversionParams(List<Rate> bankRateList, double currencyInRateValue, double currencyOutRateValue) {

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

            if (validateConversionParams(bankRateList, currencyInRateValue, currencyOutRateValue)) {

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

        String text = context.getString(R.string.field_loading_rates_);
        exchangeRatesView.showLoading(text);
        String dateText = DateUtils.getRateDateFormat().format(date);
        loadRates(dateText);
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

    public void onLoadingRatesErrorCancel() {

        exchangeRatesView.hideLoadingRatesError();
    }

    public void onLoadingRatesErrorTryAgain(String date) {

        exchangeRatesView.hideLoadingRatesError();
        loadRates(date);
    }

    public void onRetryButtonClicked() {

        loadInitialData();
    }

    public void onWhereToBuyButtonClicked() {

        String text = context.getString(R.string.field_find_branches_);
        exchangeRatesView.showLoading(text);

        int bankId = exchangeRatesView.getSelectedBankId();
        boolean onlyActive = exchangeRatesView.onlyActiveNow();
        loadBankBranches(bankId, onlyActive);
    }
}
