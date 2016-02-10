package md.fusionworks.lifehack.exchange_rates;

import java.util.Date;
import java.util.List;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.data.api.model.Branch;
import md.fusionworks.lifehack.data.repository.NewExchangeRatesRepository;
import md.fusionworks.lifehack.exchange_rates.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.ui.model.BankModel;
import md.fusionworks.lifehack.ui.model.BestExchangeModel;
import md.fusionworks.lifehack.ui.model.BranchModel;
import md.fusionworks.lifehack.ui.model.CurrencyModel;
import md.fusionworks.lifehack.ui.model.RateModel;
import md.fusionworks.lifehack.util.Converter;
import md.fusionworks.lifehack.util.DateUtils;
import md.fusionworks.lifehack.util.ExchangeRatesUtils;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import md.fusionworks.lifehack.util.rx.ObserverAdapter;

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

    private List<RateModel> rateList;
    private List<BankModel> bankList;
    private List<CurrencyModel> currencyList;

    private ExchangeRatesRepository exchangeRatesRepository;
    private NewExchangeRatesRepository newExchangeRatesRepository;

    public ExchangeRatesPresenter(ExchangeRatesContract.View exchangeRatesView, ExchangeRatesRepository exchangeRatesRepository, NewExchangeRatesRepository newExchangeRatesRepository) {

        this.exchangeRatesView = exchangeRatesView;
        this.exchangeRatesRepository = exchangeRatesRepository;
        this.newExchangeRatesRepository = newExchangeRatesRepository;
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

        newExchangeRatesRepository.getBanks()
                .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
                .subscribe(new ObserverAdapter<List<BankModel>>() {

                    @Override
                    public void onNext(List<BankModel> bankModels) {
                        bankList = bankModels;
                        exchangeRatesView.populateBankSpinner(bankModels);

                        if (isInitialDataLoaded())
                            onInitialDataLoadedSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onInitialDataLoadedError();
                    }
                });
    }

    private void loadCurrencies() {

        newExchangeRatesRepository.getCurrencies()
                .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
                .subscribe(new ObserverAdapter<List<CurrencyModel>>() {
                    @Override
                    public void onNext(List<CurrencyModel> currencyModels) {
                        currencyList = currencyModels;
                        exchangeRatesView.populateCurrencyInGroup(currencyModels);
                        exchangeRatesView.populateCurrencyOutGroup(currencyModels);

                        if (isInitialDataLoaded())
                            onInitialDataLoadedSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onInitialDataLoadedError();
                    }
                });
    }

    private void loadInitialRates(String date) {

        newExchangeRatesRepository.getRates(date)
                .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
                .subscribe(new ObserverAdapter<List<RateModel>>() {
                    @Override
                    public void onNext(List<RateModel> rateModels) {
                        rateList = rateModels;


                        if (isInitialDataLoaded()) {
                            onInitialDataLoadedSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onInitialDataLoadedError();
                    }
                });
    }

    private void loadRates(String date) {


        newExchangeRatesRepository.getRates(date)
                .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
                .subscribe(new ObserverAdapter<List<RateModel>>() {
                    @Override
                    public void onNext(List<RateModel> rateModels) {
                        rateList = rateModels;
                        exchangeRatesView.hideLoading();
                        applyConversion();
                    }

                    @Override
                    public void onError(Throwable e) {
                        exchangeRatesView.hideLoading();
                        exchangeRatesView.showNotificationToast(ExchangeRatesContract.View.NotificationToastType.ERROR, R.string.field_something_gone_wrong);

                    }
                });
    }

    private void loadBankBranches(int bankId, boolean active) {

        newExchangeRatesRepository.getBranches(bankId, active)
                .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
                .subscribe(new ObserverAdapter<List<BranchModel>>() {
                    @Override
                    public void onNext(List<BranchModel> branchModels) {
                        exchangeRatesView.populateBranchesMap(branchModels);
                        exchangeRatesView.populateBranchesList(branchModels);
                        exchangeRatesView.showBranchesLayout();
                        exchangeRatesView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
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

    private boolean validateConversionParams(List<RateModel> bankRateList, double currencyInRateValue, double currencyOutRateValue) {

        if (bankRateList.size() == 0 || currencyInRateValue == 0 || currencyOutRateValue == 0) {

            notifyNoExchangeRates();
            return false;
        }

        return true;
    }

    private BestExchangeModel convertBestExchange() {

        List<RateModel> bankRateList;
        double amountInValue;
        double currencyInRateValue;
        double currencyOutRateValue;
        double amountOutValue = 0;

        BestExchangeModel bestExchangeModel = new BestExchangeModel();
        for (BankModel bank : bankList) {

            if (bank.getId() != 1) {

                bankRateList = ExchangeRatesUtils.getBankRates(rateList, bank.getId());
                amountInValue = Converter.toDouble(exchangeRatesView.getAmountInText());
                currencyInRateValue = ExchangeRatesUtils.getCurrencyRateValue(bankRateList, exchangeRatesView.getCheckedCurrencyInId());
                currencyOutRateValue = ExchangeRatesUtils.getCurrencyRateValue(bankRateList, exchangeRatesView.getCheckedCurrencyOutId());
                double bankAmountOutValue = ExchangeRatesUtils.convert(amountInValue, currencyInRateValue, currencyOutRateValue);
                if (bankAmountOutValue > amountOutValue) {

                    amountOutValue = bankAmountOutValue;
                    bestExchangeModel = new BestExchangeModel(bank, amountOutValue);
                }
            }

            exchangeRatesView.setAmountOutText(String.format("%.2f", bestExchangeModel.getAmountOutvalue()));

            String bestExchangeBankText = (bestExchangeModel.getBank() != null) ?
                    String.format("Используется курс банка %s", bestExchangeModel.getBank().getName()) :
                    "Не найден подходящий банк";
            exchangeRatesView.setBestExchangeBankText(bestExchangeBankText);
        }

        return bestExchangeModel;
    }

    @Override
    public void applyConversion() {

        int bankId = exchangeRatesView.getSelectedBankId();
        if (bankId == 0)
            convertBestExchange();
        else
            convertBank(bankId);
    }

    private void convertBank(int bankId) {

        List<RateModel> bankRateList;
        double amountInValue;
        double currencyInRateValue;
        double currencyOutRateValue;

        bankRateList = ExchangeRatesUtils.getBankRates(rateList, bankId);
        amountInValue = Converter.toDouble(exchangeRatesView.getAmountInText());
        currencyInRateValue = ExchangeRatesUtils.getCurrencyRateValue(bankRateList, exchangeRatesView.getCheckedCurrencyInId());
        currencyOutRateValue = ExchangeRatesUtils.getCurrencyRateValue(bankRateList, exchangeRatesView.getCheckedCurrencyOutId());

        if (validateConversionParams(bankRateList, currencyInRateValue, currencyOutRateValue)) {

            double amountOutValue = ExchangeRatesUtils.convert(amountInValue, currencyInRateValue, currencyOutRateValue);
            exchangeRatesView.setAmountOutText(String.format("%.2f", amountOutValue));
        }
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

            BestExchangeModel bestExchangeModel = convertBestExchange();
            bankId = bestExchangeModel.getBank().getId();
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
    public void onShowInfoWindowAction(BranchModel branch) {

        exchangeRatesView.showInfoWindow(branch);
    }
}
