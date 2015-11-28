package md.fusionworks.lifehack.exchange_rates;

import java.util.Date;
import java.util.List;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.api.Callback;
import md.fusionworks.lifehack.entity.Bank;
import md.fusionworks.lifehack.entity.BestExchange;
import md.fusionworks.lifehack.entity.Branch;
import md.fusionworks.lifehack.entity.Currency;
import md.fusionworks.lifehack.entity.Rate;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.util.Converter;
import md.fusionworks.lifehack.util.DateUtils;
import md.fusionworks.lifehack.util.ExchangeRatesUtils;

/**
 * Created by ungvas on 11/23/15.
 */
public class ExchangeRatesPresenter implements ExchangeRatesContract.UserActionsListener {

    private static final int DEFAULT_AMOUNT_IN_VALUE = 100;
    public static final int DEFAULT_CURRENCY_IN_ID = 2;
    public static final int DEFAULT_CURRENCY_OUT_ID = 1;
    public static final int DEFAULT_BANK_ID = 2;
    public static final String NO_EXCHANGE_RATES_OUT_VALUE = "-";

    private ExchangeRatesContract.View exchangeRatesView;

    private List<Rate> rateList;
    private List<Bank> bankList;
    private List<Currency> currencyList;
    private BestExchange bestExchange;

    private ExchangeRatesRepository exchangeRatesRepository;

    public ExchangeRatesPresenter(ExchangeRatesContract.View exchangeRatesView, ExchangeRatesRepository exchangeRatesRepository) {

        this.exchangeRatesView = exchangeRatesView;
        this.exchangeRatesRepository = exchangeRatesRepository;
    }

    @Override
    public void loadInitialData() {

        exchangeRatesView.showLoading(R.string.field_loading_rates_);
        loadBanks();
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

    private void loadBanks() {

        exchangeRatesRepository.getBanks(new Callback<List<Bank>>() {
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

        exchangeRatesRepository.getCurrencies(new Callback<List<Currency>>() {
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

        exchangeRatesRepository.getRates(date, new Callback<List<Rate>>() {
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

        exchangeRatesRepository.getRates(date, new Callback<List<Rate>>() {
            @Override
            public void onSuccess(List<Rate> response) {

                rateList = response;
                exchangeRatesView.hideLoading();
                applyConversion();
            }

            @Override
            public void onError(Throwable t) {

                exchangeRatesView.hideLoading();
                exchangeRatesView.showNotificationToast(ExchangeRatesContract.View.NotificationToastType.ERROR, R.string.field_something_gone_wrong);

            }
        });
    }

    private void loadBankBranches(int bankId, boolean active) {

        exchangeRatesRepository.getBankBranches(bankId, active, new Callback<List<Branch>>() {
            @Override
            public void onSuccess(List<Branch> response) {

                exchangeRatesView.populateBranchesMap(response);
                exchangeRatesView.populateBranchesList(response);
                exchangeRatesView.showBranchesLayout();
                exchangeRatesView.hideLoading();
            }

            @Override
            public void onError(Throwable t) {

                exchangeRatesView.hideLoading();
                exchangeRatesView.showNotificationToast(ExchangeRatesContract.View.NotificationToastType.ERROR, R.string.field_something_gone_wrong);
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

        exchangeRatesView.showNotificationToast(ExchangeRatesContract.View.NotificationToastType.ERROR, R.string.field_no_rate);
        exchangeRatesView.setAmountOutText(NO_EXCHANGE_RATES_OUT_VALUE);
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
                amountInValue = Converter.toDouble(exchangeRatesView.getAmountInText());
                currencyInRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, exchangeRatesView.getCheckedCurrencyInId());
                currencyOutRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, exchangeRatesView.getCheckedCurrencyOutId());
                double bankAmountOutValue = convertBank(bankRateList, amountInValue, currencyInRateValue, currencyOutRateValue);
                if (bankAmountOutValue > amountOutValue) {

                    amountOutValue = bankAmountOutValue;
                    bestExchange = new BestExchange(bank, amountOutValue);
                }
            }

            exchangeRatesView.setAmountOutText(String.format("%.2f", bestExchange.getAmountOutvalue()));

            String bestExchangeBankText = (bestExchange.getBank() != null) ?
                    String.format("Используется курс банка %s", bestExchange.getBank().getName()) :
                    "Не найден подходящий банк";
            exchangeRatesView.setBestExchangeBankText(bestExchangeBankText);
        }

        return bestExchange;
    }

    @Override
    public void applyConversion() {

        List<Rate> bankRateList;
        double amountInValue;
        double currencyInRateValue;
        double currencyOutRateValue;

        int bankId = exchangeRatesView.getSelectedBankId();

        if (bankId == 0) {

            bestExchange = convertBestExchange();
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
    }

    private double convertBank(List<Rate> rateList, double amountInValue, double currencyInRateValue,
                               double currencyOutRateValue) {

        if (amountInValue == 0)
            return 0;

        double amountOutValue = ExchangeRatesUtils.convert(amountInValue, currencyInRateValue, currencyOutRateValue);

        return amountOutValue;
    }

    @Override
    public void onRatesDateChanged(Date date) {

        exchangeRatesView.showLoading(R.string.field_loading_rates_);
        String dateText = DateUtils.getRateDateFormat().format(date);
        loadRates(dateText);
    }

    @Override
    public void onCurrencyInChanged(int checkedId) {

        int currencyInId = checkedId;
        int currencyOutId = exchangeRatesView.getCheckedCurrencyOutId();

        if (currencyInId == currencyOutId) {

            exchangeRatesView.currencyOutCheckNext(checkedId);
        }

        applyConversion();
    }

    @Override
    public void onCurrencyOutChanged(int checkedId) {

        int currencyOutId = checkedId;
        int currencyInId = exchangeRatesView.getCheckedCurrencyInId();

        if (currencyInId == currencyOutId) {

            exchangeRatesView.currencyInCheckNext(checkedId);
        }

        applyConversion();
    }

    @Override
    public void onBankSelected(int position, long id) {

        if (position != 0)
            exchangeRatesView.setBestExchangeBankText("");

        applyConversion();
    }

    @Override
    public void onWhereToBuyAction() {

        exchangeRatesView.showLoading(R.string.field_find_branches_);

        int bankId = exchangeRatesView.getSelectedBankId();
        if (bankId == 1) {

            exchangeRatesView.setBankSelection(0);
            bankId = 0;
        }

        if (bankId == 0) {

            BestExchange bestExchange = convertBestExchange();
            bankId = bestExchange.getBank().getId();
        }

        boolean onlyActive = exchangeRatesView.onlyActiveNow();
        loadBankBranches(bankId, onlyActive);
    }

    @Override
    public void onRetryAction() {

        loadInitialData();
    }

    @Override
    public void onAmountInChanged(String text) {

        applyConversion();
    }

    @Override
    public void onShowInfoWindowAction(Branch branch) {

        exchangeRatesView.showInfoWindow(branch);
    }
}
