package md.fusionworks.lifehack.exchange_rates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;

import java.util.Date;
import java.util.List;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Bank;
import md.fusionworks.lifehack.entity.BestExchange;
import md.fusionworks.lifehack.entity.Branch;
import md.fusionworks.lifehack.entity.Currency;
import md.fusionworks.lifehack.entity.Rate;
import md.fusionworks.lifehack.exchange_rates.interactor.GetBankBranches;
import md.fusionworks.lifehack.exchange_rates.interactor.GetBankBranchesImpl;
import md.fusionworks.lifehack.exchange_rates.interactor.GetBanks;
import md.fusionworks.lifehack.exchange_rates.interactor.GetBanksImpl;
import md.fusionworks.lifehack.exchange_rates.interactor.GetCurrencies;
import md.fusionworks.lifehack.exchange_rates.interactor.GetCurrenciesImpl;
import md.fusionworks.lifehack.exchange_rates.interactor.GetRates;
import md.fusionworks.lifehack.exchange_rates.interactor.GetRatesImpl;
import md.fusionworks.lifehack.ui.Presenter;
import md.fusionworks.lifehack.util.Converter;
import md.fusionworks.lifehack.util.DateUtils;
import md.fusionworks.lifehack.util.ExchangeRatesUtils;

/**
 * Created by ungvas on 10/22/15.
 */

public class CurrencyConverterPresenter implements Presenter<CurrencyConverterView> {

    private static final int DEFAULT_AMOUNT_IN_VALUE = 100;
    public static final int DEFAULT_CURRENCY_IN_ID = 2;
    public static final int DEFAULT_CURRENCY_OUT_ID = 1;
    public static final int DEFAULT_BANK_ID = 2;
    public static final String NO_EXCHANGE_RATES_OUT_VALUE = "-";

    private Context context;
    private CurrencyConverterView currencyConverterView;

    private List<Rate> rateList;
    private List<Bank> bankList;
    private List<Currency> currencyList;

    private GetBanks getBanksUseCase;
    private GetCurrencies getCurrenciesUseCase;
    private GetRates getRatesUseCase;
    private GetBankBranches getBankBranchesUseCase;

    private BestExchange bestExchange;

    public CurrencyConverterPresenter(Context context) {
        this(context, new GetBanksImpl(context), new GetCurrenciesImpl(context), new GetRatesImpl(context), new GetBankBranchesImpl(context));
    }

    CurrencyConverterPresenter(Context context, GetBanks getBanksUseCase, GetCurrencies getCurrenciesUseCase, GetRates getRatesUseCase, GetBankBranches getBankBranchesUseCase) {

        this.context = context;
        this.getBanksUseCase = getBanksUseCase;
        this.getCurrenciesUseCase = getCurrenciesUseCase;
        this.getRatesUseCase = getRatesUseCase;
        this.getBankBranchesUseCase = getBankBranchesUseCase;
    }

    @Override
    public void attachView(@NonNull CurrencyConverterView view) {

        currencyConverterView = view;
    }

    @Override
    public void detachView(@NonNull CurrencyConverterView view) {

        currencyConverterView = null;
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
        currencyConverterView.showLoading(text);
        loadBanksUseCase();
        loadCurrenciesUseCase();
        loadTodayRates();
    }

    private boolean isInitialDataLoaded() {

        return (bankList != null && currencyList != null && rateList != null);
    }

    private void onInitialDataLoadedSuccess() {

        currencyConverterView.hideRetryView();
        currencyConverterView.showExchangeRatesView();
        currencyConverterView.hideLoading();
        setupUIWithDefaultValues();
    }

    private void onInitialDataLoadedError() {

        currencyConverterView.hideLoading();
        currencyConverterView.hideExchangeRatesView();
        currencyConverterView.showRetryView();
    }

    private void loadBanksUseCase() {

        getBanksUseCase.execute(new Callback<List<Bank>>() {
            @Override
            public void onSuccess(List<Bank> response) {

                bankList = response;
                currencyConverterView.populateBankSpinner(response);

                if (isInitialDataLoaded())
                    onInitialDataLoadedSuccess();
            }

            @Override
            public void onError(Throwable t) {

                onInitialDataLoadedError();
            }
        });
    }

    private void loadCurrenciesUseCase() {

        getCurrenciesUseCase.execute(new Callback<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> response) {

                currencyList = response;
                currencyConverterView.populateCurrencyInGroup(response);
                currencyConverterView.populateCurrencyOutGroup(response);

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

        getRatesUseCase.execute(date, new Callback<List<Rate>>() {
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


        getRatesUseCase.execute(date, new Callback<List<Rate>>() {
            @Override
            public void onSuccess(List<Rate> response) {

                rateList = response;
                currencyConverterView.hideLoading();
                applyConversion();
            }

            @Override
            public void onError(Throwable t) {

                currencyConverterView.hideLoading();
                currencyConverterView.showLoadingRatesError(date);
            }
        });
    }

    private void loadBankBranches(int bankId, boolean active) {

        getBankBranchesUseCase.execute(bankId, active, new Callback<List<Branch>>() {
            @Override
            public void onSuccess(List<Branch> response) {

                currencyConverterView.populateBranchesList(response);
                currencyConverterView.showBranchesLayout();
                currencyConverterView.hideLoading();
            }

            @Override
            public void onError(Throwable t) {

                currencyConverterView.hideLoading();
            }
        });
    }

    private void loadTodayRates() {

        String today = DateUtils.getRateDateFormat().format(new Date());
        loadInitialRates(today);
    }

    private void setupUIWithDefaultValues() {

        currencyConverterView.setAmountInText(String.valueOf(DEFAULT_AMOUNT_IN_VALUE));
        currencyConverterView.setCurrencyInCheckedById(DEFAULT_CURRENCY_IN_ID);
        currencyConverterView.setCurrencyOutCheckedById(DEFAULT_CURRENCY_OUT_ID);
        currencyConverterView.setBankSelection(DEFAULT_BANK_ID);
        currencyConverterView.initializeViewListeners();
    }

    private void notifyNoExchangeRates() {

        currencyConverterView.showNotificationToast("Нету курса",
                CurrencyConverterView.NotificationToastType.ERROR);
        currencyConverterView.setAmountOutText(NO_EXCHANGE_RATES_OUT_VALUE);
    }

    private boolean validateConversionParams(List<Rate> bankRateList, double currencyInRateValue, double currencyOutRateValue) {

        if (bankRateList.size() == 0 || currencyInRateValue == 0 || currencyOutRateValue == 0) {

            notifyNoExchangeRates();
            return false;
        }

        return true;
    }

    private BestExchange convertBestExchange() {

        List<Rate> bankRateList;
        double amountInValue;
        double currencyInRateValue;
        double currencyOutRateValue;
        double amountOutValue = 0;

        BestExchange bestExchange = new BestExchange();
        for (Bank bank : bankList) {

            if (bank.getId() != 1) {

                bankRateList = ExchangeRatesUtils.getBankRates(rateList, bank.getId());
                amountInValue = Converter.toDouble(currencyConverterView.getAmountInText());
                currencyInRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, currencyConverterView.getCheckedCurrencyInId());
                currencyOutRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, currencyConverterView.getCheckedCurrencyOutId());
                double bankAmountOutValue = convertBank(bankRateList, amountInValue, currencyInRateValue, currencyOutRateValue);
                if (bankAmountOutValue > amountOutValue) {

                    amountOutValue = bankAmountOutValue;
                    bestExchange = new BestExchange(bank, amountOutValue);
                }
            }

            currencyConverterView.setAmountOutText(String.format("%.2f", bestExchange.getAmountOutvalue()));

            String bestExchangeBankText = (bestExchange.getBank() != null) ?
                    String.format("Используется курс банка %s", bestExchange.getBank().getName()) :
                    "Не найден подходящий банк";
            currencyConverterView.setBestExchangeBankText(bestExchangeBankText);
        }

        return bestExchange;
    }


    public void applyConversion() {

        List<Rate> bankRateList;
        double amountInValue;
        double currencyInRateValue;
        double currencyOutRateValue;

        int bankId = currencyConverterView.getSelectedBankId();

        if (bankId == 0) {

            bestExchange = convertBestExchange();
        } else {

            bankRateList = ExchangeRatesUtils.getBankRates(rateList, currencyConverterView.getSelectedBankId());
            amountInValue = Converter.toDouble(currencyConverterView.getAmountInText());
            currencyInRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, currencyConverterView.getCheckedCurrencyInId());
            currencyOutRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, currencyConverterView.getCheckedCurrencyOutId());

            if (validateConversionParams(bankRateList, currencyInRateValue, currencyOutRateValue)) {

                double amountOutValue = convertBank(bankRateList, amountInValue, currencyInRateValue, currencyOutRateValue);
                currencyConverterView.setAmountOutText(String.format("%.2f", amountOutValue));
            }
        }
    }

    private double convertBank(List<Rate> rateList, double amountInValue, double currencyInRateValue,
                               double currencyOutRateValue) {

        if (amountInValue == 0)
            return 0;

        double amountOutValue = ExchangeRatesUtils.convert(amountInValue, currencyInRateValue, currencyOutRateValue);

        return amountOutValue;
    }

    public void onRatesDateChanged(Date date) {

        String text = context.getString(R.string.field_loading_rates_);
        currencyConverterView.showLoading(text);
        String dateText = DateUtils.getRateDateFormat().format(date);
        loadRates(dateText);
    }

    public void onCurrencyInChanged(RadioGroup radioGroup, int checkedId) {

        int currencyInId = checkedId;
        int currencyOutId = currencyConverterView.getCheckedCurrencyOutId();

        if (currencyInId == currencyOutId) {

            currencyConverterView.currencyOutCheckNext(checkedId);
        }

        applyConversion();
    }

    public void onCurrencyOutChanged(RadioGroup radioGroup, int checkedId) {

        int currencyOutId = checkedId;
        int currencyInId = currencyConverterView.getCheckedCurrencyInId();

        if (currencyInId == currencyOutId) {

            currencyConverterView.currencyInCheckNext(checkedId);
        }

        applyConversion();
    }

    public void afterAmountInTextChanged(String text) {

        applyConversion();
    }

    public void onBankSelected(int position, long id) {

        if (position != 0)
            currencyConverterView.setBestExchangeBankText("");

        applyConversion();
    }

    public void onLoadingRatesErrorCancel() {

        currencyConverterView.hideLoadingRatesError();
    }

    public void onLoadingRatesErrorTryAgain(String date) {

        currencyConverterView.hideLoadingRatesError();
        loadRates(date);
    }

    public void onRetryButtonClicked() {

        loadInitialData();
    }

    public void onWhereToBuyButtonClicked() {

        String text = context.getString(R.string.field_find_branches_);
        currencyConverterView.showLoading(text);

        int bankId = currencyConverterView.getSelectedBankId();
        if (bankId == 1) {

            currencyConverterView.setBankSelection(0);
            bankId = 0;
        }

        if (bankId == 0) {

            BestExchange bestExchange = convertBestExchange();
            bankId = bestExchange.getBank().getId();
        }

        boolean onlyActive = currencyConverterView.onlyActiveNow();
        loadBankBranches(bankId, onlyActive);
    }
}
