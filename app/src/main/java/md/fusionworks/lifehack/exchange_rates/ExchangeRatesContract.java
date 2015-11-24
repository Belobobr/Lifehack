package md.fusionworks.lifehack.exchange_rates;

import android.widget.RadioGroup;

import java.util.Date;
import java.util.List;

import md.fusionworks.lifehack.entity.Bank;
import md.fusionworks.lifehack.entity.Branch;
import md.fusionworks.lifehack.entity.Currency;

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

        void setBankSelectionById(int bankId);

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

        void showBranchesLayout();

        void populateBranchesList(List<Branch> branchList);

        void populateBranchesMap(List<Branch> branchList);

        void showInfoWindow(Branch branch);
    }

    interface UserActionsListener {

        void loadInitialData();

        void applyConversion();

        void showWhereToBuyBranches();

        void tryAgainLoadingRates(String date);

        void cancelLoadingRates();

        void applyConversionOnBankSelected(int position, long id);

        void applyConversionOnCurrencyOutChanged(RadioGroup radioGroup, int checkedId);

        void applyConversionOnCurrencyInChanged(RadioGroup radioGroup, int checkedId);

        void applyConversionOnRatesDateChanged(Date date);

        void showInfoWindow(Branch branch);
    }
}
