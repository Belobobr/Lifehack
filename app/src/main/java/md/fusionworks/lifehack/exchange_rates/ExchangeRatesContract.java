package md.fusionworks.lifehack.exchange_rates;

import java.util.Date;
import java.util.List;

import md.fusionworks.lifehack.ui.model.exchange_rates.BankModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.BranchModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.CurrencyModel;

/**
 * Created by ungvas on 11/23/15.
 */
public interface ExchangeRatesContract {

  interface View {

    enum NotificationToastType {
      INFO, ERROR
    }

    void showLoading(int resourceId);

    void hideLoading();

    void showNotificationToast(int type, int stringResId);

    void setAmountInText(String text);

    String getAmountInText();

    void setAmountOutText(String text);

    void initializeViewListeners();

    void populateBankSpinner(List<BankModel> bankList);

    int getSelectedBankId();

    void setBankSelection(int position);

    void setBankSelectionById(int bankId);

    void setBestExchangeBankText(String text);

    void populateCurrencyInGroup(List<CurrencyModel> currencyList);

    void populateCurrencyOutGroup(List<CurrencyModel> currencyList);

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

    void showBranchesLayout();

    void populateBranchesList(List<BranchModel> branchList);

    void populateBranchesMap(List<BranchModel> branchList);

    void showInfoWindow(BranchModel branch);
  }

  interface UserActionsListener {

    void loadInitialData();

    void applyConversion();

    void onWhereToBuyAction();

    void onBankSelected(int position, long id);

    void onCurrencyOutChanged(int checkedId);

    void onCurrencyInChanged(int checkedId);

    void onRatesDateChanged(Date date);

    void onRetryAction();

    void onAmountInChanged(String text);

    void onShowInfoWindowAction(BranchModel branch);
  }
}
