package md.fusionworks.lifehack.ui.exchange_rates.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import com.badoo.mobile.util.WeakHandler;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.Date;
import java.util.List;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.data.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.ui.BaseFragment;
import md.fusionworks.lifehack.ui.exchange_rates.BankSpinnerAdapter;
import md.fusionworks.lifehack.ui.exchange_rates.event.WhereToBuyEvent;
import md.fusionworks.lifehack.ui.exchange_rates.model.BankModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.BankSpinnerItemModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.BestExchangeModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.CurrencyModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.InitialDataModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.RateModel;
import md.fusionworks.lifehack.ui.widget.CurrenciesGroup;
import md.fusionworks.lifehack.ui.widget.DateView;
import md.fusionworks.lifehack.ui.widget.RetryView;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.Converter;
import md.fusionworks.lifehack.util.DateUtil;
import md.fusionworks.lifehack.util.ExchangeRatesUtil;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import md.fusionworks.lifehack.util.rx.ObserverAdapter;
import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeRatesFragment extends BaseFragment {

  @Bind(R.id.amountInField) EditText amountInField;
  @Bind(R.id.amountOutField) EditText amountOutField;
  @Bind(R.id.bankSpinner) Spinner bankSpinner;
  @Bind(R.id.bestExchangeBankField) TextView bestExchangeBankField;
  @Bind(R.id.ratesDateField) DateView ratesDateField;
  @Bind(R.id.currencyInRadioGroup) CurrenciesGroup currenciesInGroup;
  @Bind(R.id.currencyOutRadioGroup) CurrenciesGroup currenciesOutGroup;
  @Bind(R.id.exchangeRatesView) LinearLayout exchangeRatesView;
  @Bind(R.id.retryView) RetryView retryView;
  @Bind(R.id.retryButton) Button retryButton;
  @Bind(R.id.whereToBuyButton) TextView whereToBuyButton;
  @Bind(R.id.onlyActiveNowCheckBox) CheckBox onlyActiveNowCheckBox;

  private List<RateModel> rateList;
  private List<BankModel> bankList;
  private List<CurrencyModel> currencyList;

  private ExchangeRatesRepository exchangeRatesRepository;
  private BankSpinnerAdapter bankSpinnerAdapter;
  private WeakHandler weakHandler;

  public ExchangeRatesFragment() {
  }

  public static ExchangeRatesFragment newInstance() {
    return new ExchangeRatesFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    exchangeRatesRepository = new ExchangeRatesRepository();
    bankSpinnerAdapter = new BankSpinnerAdapter(getActivity());
    weakHandler = new WeakHandler();
  }

  @Override public void onDestroy() {
    weakHandler.removeCallbacksAndMessages(null);
    super.onDestroy();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflateAndBindViews(inflater, R.layout.fragment_exchange_rates, container);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    loadInitialData();
  }

  private void loadInitialData() {
    showLoadingDialog();
    Observable<List<BankModel>> bankObservable = exchangeRatesRepository.getBanks();

    Observable<List<CurrencyModel>> currencyObservable = exchangeRatesRepository.getCurrencies();

    String today = DateUtil.getRateDateFormat().format(new Date());
    Observable<List<RateModel>> rateObservable = exchangeRatesRepository.getRates(today);

    Observable.zip(bankObservable, currencyObservable, rateObservable,
        (bankModels, currencyModels, rateModels) -> new InitialDataModel(bankModels, currencyModels,
            rateModels))
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .subscribe(new ObserverAdapter<InitialDataModel>() {
          @Override public void onNext(InitialDataModel initialDataModel) {
            bankList = initialDataModel.getBankModelList();
            currencyList = initialDataModel.getCurrencyModelList();
            rateList = initialDataModel.getRateModelList();
            onInitialDataLoadedSuccess();
          }

          @Override public void onError(Throwable e) {
            onInitialDataLoadedError();
          }
        });
  }

  private void loadRates(String date) {
    showLoadingDialog();
    exchangeRatesRepository.getRates(date)
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .subscribe(new ObserverAdapter<List<RateModel>>() {
          @Override public void onNext(List<RateModel> rateModels) {
            rateList = rateModels;
            hideLoadingDialog();
            applyConversion();
          }

          @Override public void onError(Throwable e) {
            hideLoadingDialog();
            showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                getString(R.string.field_something_gone_wrong));
          }
        });
  }

  private void onInitialDataLoadedSuccess() {
    hideLoadingDialog();
    hideRetryView();
    showExchangeRatesView();
    populateCurrencyInGroup(currencyList);
    populateCurrencyOutGroup(currencyList);
    populateBankSpinner(bankList);
    setupUIWithDefaultValues();
    initializeViewListeners();
  }

  private void onInitialDataLoadedError() {
    hideLoadingDialog();
    hideExchangeRatesView();
    showRetryView();
  }

  private void showExchangeRatesView() {
    exchangeRatesView.setVisibility(View.VISIBLE);
  }

  private void hideExchangeRatesView() {
    exchangeRatesView.setVisibility(View.GONE);
  }

  private void showRetryView() {
    retryView.show();
    retryView.setOnRetryActionListener(() -> {
      retryView.hide();
      loadInitialData();
    });
  }

  private void hideRetryView() {
    retryView.hide();
  }

  private void populateCurrencyInGroup(List<CurrencyModel> currencyList) {
    currenciesInGroup.addCurrencies(currencyList);
  }

  private void populateCurrencyOutGroup(List<CurrencyModel> currencyList) {
    currenciesOutGroup.addCurrencies(currencyList);
  }

  private void populateBankSpinner(List<BankModel> bankList) {
    bankSpinnerAdapter.clear();
    bankSpinnerAdapter.addItem(getActivity().getString(R.string.spinner_option_best_exchange), 0);
    bankSpinnerAdapter.addHeader(getActivity().getString(R.string.spinner_option_bank_list));
    for (BankModel bank : bankList) {
      bankSpinnerAdapter.addItem(bank.name, bank.id);
    }
    bankSpinner.setAdapter(bankSpinnerAdapter);
  }

  private void setupUIWithDefaultValues() {
    setAmountInText(String.valueOf(Constant.DEFAULT_AMOUNT_IN_VALUE));
    setCurrencyInCheckedById(Constant.DEFAULT_CURRENCY_IN_ID);
    setCurrencyOutCheckedById(Constant.DEFAULT_CURRENCY_OUT_ID);
    setBankSelection(Constant.DEFAULT_BANK_ID);
    initializeViewListeners();
  }

  private void setAmountInText(String text) {
    amountInField.setText(text);
  }

  private String getAmountInText() {
    return amountInField.getText().toString();
  }

  private void setAmountOutText(String text) {
    amountOutField.setText(text);
  }

  private void setCurrencyInCheckedById(int currencyId) {
    currenciesInGroup.setCurrencyCheckedById(currencyId);
  }

  private void setCurrencyOutCheckedById(int currencyId) {
    currenciesOutGroup.setCurrencyCheckedById(currencyId);
  }

  private void setBankSelection(int position) {
    bankSpinner.setSelection(position);
  }

  private void initializeViewListeners() {
    getCompositeSubscription().add(RxTextView.textChanges(amountInField).subscribe(charSequence -> {
      applyConversion();
    }));

    getCompositeSubscription().add(RxAdapterView.itemSelections(bankSpinner).subscribe(position -> {
      if (position != Constant.BEST_EXCHANGE_BANK_ID) setBestExchangeBankText("");
      applyConversion();
    }));

    ratesDateField.setOnDateChangedListener(date -> {
      String dateText = DateUtil.getRateDateFormat().format(date);
      loadRates(dateText);
    });

    currenciesInGroup.setOnCheckedChangeListener(
        (group, checkedId) -> onCurrencyInChanged(checkedId));

    currenciesOutGroup.setOnCheckedChangeListener(
        (group, checkedId) -> onCurrencyOutChanged(checkedId));

    whereToBuyButton.setOnClickListener(v -> {
      amountInField.clearFocus();
      onWhereToBuyAction();
    });

    onlyActiveNowCheckBox.setOnClickListener(v -> amountInField.clearFocus());

    bankSpinner.setOnTouchListener((v, event) -> {
      amountInField.clearFocus();
      return false;
    });

    ratesDateField.setOnTouchListener((v, event) -> {
      amountInField.clearFocus();
      return false;
    });
  }

  private void onCurrencyInChanged(int checkedId) {
    amountInField.clearFocus();
    int currencyInId = checkedId;
    int currencyOutId = getCheckedCurrencyOutId();
    if (currencyInId == currencyOutId) {
      currencyOutCheckNext(checkedId);
    }
    applyConversion();
  }

  private void onCurrencyOutChanged(int checkedId) {
    amountInField.clearFocus();
    int currencyOutId = checkedId;
    int currencyInId = getCheckedCurrencyInId();
    if (currencyInId == currencyOutId) {
      currencyInCheckNext(checkedId);
    }
    applyConversion();
  }

  private int getCheckedCurrencyInId() {
    return currenciesInGroup.getCheckedRadioButtonId();
  }

  private int getCheckedCurrencyOutId() {
    return currenciesOutGroup.getCheckedRadioButtonId();
  }

  private void currencyInCheckNext(int checkedId) {
    currenciesInGroup.checkNextCurrency(checkedId);
  }

  private void currencyOutCheckNext(int checkedId) {
    currenciesOutGroup.checkNextCurrency(checkedId);
  }

  private void notifyNoExchangeRates() {
    showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR, getString(R.string.field_no_rate));
    setAmountOutText(Constant.NO_EXCHANGE_RATES_OUT_VALUE);
  }

  private boolean validateConversionParams(List<RateModel> bankRateList, double currencyInRateValue,
      double currencyOutRateValue) {
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
      if (bank.id != 1) {
        bankRateList = ExchangeRatesUtil.getBankRates(rateList, bank.id);
        amountInValue = Converter.toDouble(getAmountInText());
        currencyInRateValue =
            ExchangeRatesUtil.getCurrencyRateValue(bankRateList, getCheckedCurrencyInId());
        currencyOutRateValue =
            ExchangeRatesUtil.getCurrencyRateValue(bankRateList, getCheckedCurrencyOutId());
        double bankAmountOutValue =
            ExchangeRatesUtil.convert(amountInValue, currencyInRateValue, currencyOutRateValue);
        if (bankAmountOutValue > amountOutValue) {
          amountOutValue = bankAmountOutValue;
          bestExchangeModel = new BestExchangeModel(bank, amountOutValue);
        }
      }

      setAmountOutText(String.format("%.2f", bestExchangeModel.getAmountOutvalue()));

      String bestExchangeBankText = (bestExchangeModel.getBank() != null) ? String.format(
          getActivity().getString(R.string.field_best_bank), bestExchangeModel.getBank().name)
          : getActivity().getString(R.string.field_bank_not_found);
      setBestExchangeBankText(bestExchangeBankText);
    }

    return bestExchangeModel;
  }

  private void applyConversion() {
    int bankId = getSelectedBankId();
    if (bankId == Constant.BEST_EXCHANGE_BANK_ID) {
      convertBestExchange();
    } else {
      convertBank(bankId);
    }
  }

  private void convertBank(int bankId) {

    List<RateModel> bankRateList;
    double amountInValue;
    double currencyInRateValue;
    double currencyOutRateValue;

    bankRateList = ExchangeRatesUtil.getBankRates(rateList, bankId);
    amountInValue = Converter.toDouble(getAmountInText());
    currencyInRateValue =
        ExchangeRatesUtil.getCurrencyRateValue(bankRateList, getCheckedCurrencyInId());
    currencyOutRateValue =
        ExchangeRatesUtil.getCurrencyRateValue(bankRateList, getCheckedCurrencyOutId());

    if (validateConversionParams(bankRateList, currencyInRateValue, currencyOutRateValue)) {
      double amountOutValue =
          ExchangeRatesUtil.convert(amountInValue, currencyInRateValue, currencyOutRateValue);
      setAmountOutText(String.format("%.2f", amountOutValue));
    }
  }

  private void setBestExchangeBankText(String text) {
    bestExchangeBankField.setVisibility((text.length() > 0) ? View.VISIBLE : View.GONE);
    bestExchangeBankField.setText(text);
  }

  private int getSelectedBankId() {
    BankSpinnerItemModel bankSpinnerItemModel =
        (BankSpinnerItemModel) bankSpinner.getSelectedItem();
    return bankSpinnerItemModel.getBankId();
  }

  private void onWhereToBuyAction() {
    showLoadingDialog();
    int bankId = getSelectedBankId();
    if (bankId == Constant.BNM_BANK_ID) {
      setBankSelection(Constant.BEST_EXCHANGE_BANK_ID);
      bankId = Constant.BEST_EXCHANGE_BANK_ID;
    }
    if (bankId == Constant.BEST_EXCHANGE_BANK_ID) {
      BestExchangeModel bestExchangeModel = convertBestExchange();
      if (bestExchangeModel.getBank() == null) {
        hideLoadingDialog();
        return;
      }
      bankId = bestExchangeModel.getBank().id;
    }

    boolean onlyActive = onlyActiveNow();
    loadBankBranches(bankId, onlyActive);
  }

  private boolean onlyActiveNow() {
    return onlyActiveNowCheckBox.isChecked();
  }

  private void loadBankBranches(int bankId, boolean active) {
    showLoadingDialog();
    exchangeRatesRepository.getBranches(bankId, active)
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .subscribe(new ObserverAdapter<List<BranchModel>>() {
          @Override public void onNext(List<BranchModel> branchModels) {
            hideLoadingDialog();
            getRxBus().postIfHasObservers(new WhereToBuyEvent(branchModels));
          }

          @Override public void onError(Throwable e) {
            hideLoadingDialog();
            showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                getString(R.string.field_something_gone_wrong));
          }
        });
  }
}
