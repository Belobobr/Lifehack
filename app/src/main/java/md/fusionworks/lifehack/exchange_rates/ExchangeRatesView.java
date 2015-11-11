package md.fusionworks.lifehack.exchange_rates;

import java.util.List;

import md.fusionworks.lifehack.entity.Bank;
import md.fusionworks.lifehack.entity.Currency;
import md.fusionworks.lifehack.ui.View;

/**
 * Created by ungvas on 10/22/15.
 */
public interface ExchangeRatesView extends View {

    enum NotificationToastType {
        INFO, ERROR
    }

    void showLoading(String text);

    void hideLoading();

    void showLoadingRatesError(String date);

    void hideLoadingRatesError();

    void showNotificationToast(String message, NotificationToastType type);

    void setAmountInText(String text);

    String getAmountInText();

    void setAmountOutText(String text);

    void initializeViewListeners();

    void populateBankSpinner(List<Bank> bankList);

    int getSelectedBankId();

    void setBankSelection(int position);

    void setBestExchangeBankText(String text);

    void populateCurrencyInGroup(List<Currency> currencyList);

    void populateCurrencyOutGroup(List<Currency> currencyList);

    int getCheckedCurrencyInId();

    int getCheckedCurrencyOutId();

    void currencyInCheckNext(int checkedId);

    void currencyOutCheckNext(int checkedId);

    void setCurrencyInCheckedById(int currencyId);

    void setCurrencyOutCheckedById(int currencyId);

    void showExchangeRatesView();

    void hideExchangeRatesView();

    void showRetryView();

    void hideRetryView();

    boolean onlyActiveNow();
}
