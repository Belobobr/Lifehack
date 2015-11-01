package md.fusionworks.lifehack.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import md.fusionworks.lifehack.api.LifehackClient;
import md.fusionworks.lifehack.api.ServiceCreator;
import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.BestExchange;
import md.fusionworks.lifehack.model.Currency;
import md.fusionworks.lifehack.model.Rate;
import md.fusionworks.lifehack.ui.view.ExchangeRatesView;
import md.fusionworks.lifehack.util.DateUtils;
import md.fusionworks.lifehack.util.ExchangeRatesUtils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ungvas on 10/22/15.
 */
@PerActivity
public class ExchangeRatesPresenter implements Presenter<ExchangeRatesView> {

    private static final int DEFAULT_AMOUNT_IN_VALUE = 100;

    private Context context;
    private ExchangeRatesView exchangeRatesView;

    private LifehackClient lifehackClient;
    private List<Rate> rateList;
    private List<Bank> bankList;
    private List<Currency> currencyList;


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

        exchangeRatesView.showLoading();

        Call<List<Bank>> call = lifehackClient.getBanks();
        call.enqueue(new Callback<List<Bank>>() {
            @Override
            public void onResponse(Response<List<Bank>> response, Retrofit retrofit) {

                if (response.isSuccess()) {

                    bankList = response.body();
                    loadTodayRates();
                } else {

                    exchangeRatesView.hideLoading();
                    exchangeRatesView.showLoadingError();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                exchangeRatesView.hideLoading();
                exchangeRatesView.showLoadingError();
            }
        });
    }

    private void loadRates(String date, boolean setupViewByDefault) {

        exchangeRatesView.showLoading();

        Call<List<Rate>> call = lifehackClient.getRates(date);
        call.enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Response<List<Rate>> response, Retrofit retrofit) {

                if (response.isSuccess()) {

                    rateList = response.body();

                    if (setupViewByDefault)
                        setupViewByDefault();
                    else
                        convert();

                    exchangeRatesView.hideLoading();
                } else {

                    exchangeRatesView.hideLoading();
                    exchangeRatesView.showLoadingError();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                exchangeRatesView.hideLoading();
                exchangeRatesView.showLoadingError();
            }
        });
    }

    private void loadTodayRates() {

        String today = DateUtils.getRateDateFormat().format(new Date());
        loadRates(today, true);
    }

    private void setupViewByDefault() {

        currencyList = new ArrayList<>();

        currencyList.add(new Currency(1, "MDL"));
        currencyList.add(new Currency(2, "EUR"));
        currencyList.add(new Currency(3, "USD"));
        currencyList.add(new Currency(4, "RON"));
        currencyList.add(new Currency(5, "RUB"));

        exchangeRatesView.setAmountInValue(String.valueOf(DEFAULT_AMOUNT_IN_VALUE));
        exchangeRatesView.populateCurrencyInRadioGroup(currencyList);
        exchangeRatesView.populateCurrencyOutRadioGroup(currencyList);
        exchangeRatesView.setCurrencyInRadioGroupItemChecked(1);
        exchangeRatesView.setCurrencyOutRadioGroupChecked(0);
        exchangeRatesView.populateBankSpinner(bankList);
        exchangeRatesView.setBankSelection(2);
        exchangeRatesView.initializeViewListeners();
    }

    public void convert() {

        int bankId = exchangeRatesView.getSelectedBankId();
        double amountInValue = Double.valueOf(exchangeRatesView.getAmountInValue());
        int currencyInId = exchangeRatesView.getCheckedCurrencyInId();
        int currencyOutId = exchangeRatesView.getCheckedCurrencyOutId();

        boolean bestExchangeOption = bankId == 0;
        if (bestExchangeOption) {

            BestExchange bestExchange = convertBestExchange(rateList, amountInValue, currencyInId, currencyOutId);
            exchangeRatesView.setAmountOutValue(String.format("%.2f", bestExchange.getAmountOutvalue()));
            exchangeRatesView.setBestExchangeBankText(String.format("Используется курс банка %s", bestExchange.getBank().getName()));
        } else {

            double amountOutValue = convert(rateList, amountInValue, currencyInId, currencyOutId, bankId);
            exchangeRatesView.setAmountOutValue(String.format("%.2f", amountOutValue));
            exchangeRatesView.setBestExchangeBankText("");
        }
    }

    private double convert(List<Rate> rateList, double amountInValue, int bankId, int currencyInId, int currencyOutId) {

        List<Rate> bankRateList = ExchangeRatesUtils.getBankRates(rateList, bankId);
        double currencyInRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, currencyInId);
        double currencyOutRateValue = ExchangeRatesUtils.getCurrencyRateList(bankRateList, currencyOutId);
        double amountOutValue = ExchangeRatesUtils.convert(amountInValue, currencyInRateValue, currencyOutRateValue);

        return amountOutValue;
    }

    private BestExchange convertBestExchange(List<Rate> rateList, double amountInValue, int currencyInId, int currencyOutId) {

        double amountOutValue = 0;
        BestExchange bestExchange = new BestExchange();
        for (Bank bank : bankList) {

            if (bank.getId() != 1) {
                double bankAmountOutValue = convert(rateList, amountInValue, bank.getId(), currencyInId, currencyOutId);
                if (bankAmountOutValue > amountOutValue) {

                    amountOutValue = bankAmountOutValue;
                    bestExchange = new BestExchange(bank, amountOutValue);
                }
            }
        }

        return bestExchange;
    }

    public void onRatesDateChanged(Date date) {

        String dateText = DateUtils.getRateDateFormat().format(date);
        loadRates(dateText, false);
    }

    private boolean currencyTheSame() {

        int currencyInId = exchangeRatesView.getCheckedCurrencyInId();
        int currencyOutId = exchangeRatesView.getCheckedCurrencyOutId();

        if (currencyInId == currencyOutId) {

            return true;
        }

        return false;
    }
}
