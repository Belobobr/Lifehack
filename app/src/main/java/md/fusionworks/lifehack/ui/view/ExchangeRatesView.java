package md.fusionworks.lifehack.ui.view;

import android.widget.Spinner;

import java.util.List;

import md.fusionworks.lifehack.model.Bank;

/**
 * Created by ungvas on 10/22/15.
 */
public interface ExchangeRatesView extends View {

    void showLoading();

    void hideLoading();

    void showLoadingError();

    void setAmountInValue(String value);

    String getAmountInValue();

    void setAmountOutValue(String value);

    void initializeViewListeners();

    void populateBankSpinner(List<Bank> bankList);

    int getSelectedBankId();

    void setBankSelection(int position);

    void setBestExchangeBankText(String text);
}
