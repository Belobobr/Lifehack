package md.fusionworks.lifehack.exchange_rates;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.entity.Bank;
import md.fusionworks.lifehack.entity.BankSpinnerItem;
import md.fusionworks.lifehack.entity.Branch;
import md.fusionworks.lifehack.entity.Currency;
import md.fusionworks.lifehack.ui.BaseFragment;
import md.fusionworks.lifehack.ui.widget.CurrenciesGroup;
import md.fusionworks.lifehack.ui.widget.DateView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrencyConverterFragment extends BaseFragment implements CurrencyConverterView {

    @Bind(R.id.amountInField)
    EditText amountInField;
    @Bind(R.id.amountOutField)
    EditText amountOutField;
    @Bind(R.id.bankSpinner)
    Spinner bankSpinner;
    @Bind(R.id.bestExchangeBankField)
    TextView bestExchangeBankField;
    @Bind(R.id.ratesDateField)
    DateView ratesDateField;
    @Bind(R.id.currencyInRadioGroup)
    CurrenciesGroup currenciesInGroup;
    @Bind(R.id.currencyOutRadioGroup)
    CurrenciesGroup currenciesOutGroup;
    @Bind(R.id.exchangeRatesView)
    LinearLayout exchangeRatesView;
    @Bind(R.id.retryView)
    RelativeLayout retryView;
    @Bind(R.id.retryButton)
    Button retryButton;
    @Bind(R.id.whereToBuyButton)
    TextView whereToBuyButton;
    @Bind(R.id.onlyActiveNowCheckBox)
    CheckBox onlyActiveNowCheckBox;
    @Bind(R.id.branchesLayout)
    LinearLayout branchesLayout;
    @Bind(R.id.branchesListLayout)
    LinearLayout branchesListLayout;

    CurrencyConverterPresenter currencyConverterPresenter;
    BankSpinnerAdapter bankSpinnerAdapter;

    private MaterialDialog loadingInitialDataDialog;
    private MaterialDialog loadingRatesErrorDialog;

    public static CurrencyConverterFragment newInstance() {

        return new CurrencyConverterFragment();
    }

    public CurrencyConverterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_currency_converter, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
        setupUI();
    }

    private void initialize() {

        currencyConverterPresenter = new CurrencyConverterPresenter(getActivity());
        currencyConverterPresenter.attachView(this);
        currencyConverterPresenter.initialize();
    }

    private void setupUI() {

        bankSpinnerAdapter = new BankSpinnerAdapter(getActivity());
        retryButton.setOnClickListener(v -> {

            currencyConverterPresenter.onRetryButtonClicked();
        });
    }

    @Override
    public void showLoading(String text) {

        if (loadingInitialDataDialog == null) {
            loadingInitialDataDialog = new MaterialDialog.Builder(getActivity())
                    .content(text)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(true)
                    .show();
        }

        if (!loadingInitialDataDialog.isShowing())
            loadingInitialDataDialog.show();
    }

    @Override
    public void hideLoading() {

        if (loadingInitialDataDialog != null)
            if (loadingInitialDataDialog.isShowing())
                loadingInitialDataDialog.hide();

        loadingInitialDataDialog = null;
    }

    @Override
    public void showLoadingRatesError(String date) {

        if (loadingRatesErrorDialog == null) {

            loadingRatesErrorDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.field_title_load_rates_error)
                    .content(R.string.field_message_load_rates_error)
                    .positiveText(R.string.field_try_again)
                    .negativeText(R.string.field_cancel)
                    .cancelable(false)
                    .onNegative((materialDialog, dialogAction) -> {

                        currencyConverterPresenter.onLoadingRatesErrorCancel();
                    })
                    .onPositive((materialDialog, dialogAction) -> currencyConverterPresenter.onLoadingRatesErrorTryAgain(date))
                    .show();
        }

        if (!loadingRatesErrorDialog.isShowing())
            loadingRatesErrorDialog.show();
    }

    @Override
    public void hideLoadingRatesError() {

        if (loadingRatesErrorDialog != null)
            if (loadingRatesErrorDialog.isShowing())
                loadingRatesErrorDialog.hide();

        loadingRatesErrorDialog = null;
    }

    @Override
    public void setAmountInText(String text) {

        amountInField.setText(text);
    }

    @Override
    public String getAmountInText() {

        return amountInField.getText().toString();
    }

    @Override
    public void setAmountOutText(String text) {

        amountOutField.setText(text);
    }

    @Override
    public void initializeViewListeners() {

        amountInField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                currencyConverterPresenter.afterAmountInTextChanged(s.toString());
            }
        });

        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currencyConverterPresenter.onBankSelected(position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ratesDateField.setOnDateChangedListener(date -> {

            currencyConverterPresenter.onRatesDateChanged(date);
        });

        currenciesInGroup.setOnCheckedChangeListener((group, checkedId) -> {

            currencyConverterPresenter.onCurrencyInChanged(group, checkedId);
        });


        currenciesOutGroup.setOnCheckedChangeListener((group, checkedId) -> {

            currencyConverterPresenter.onCurrencyOutChanged(group, checkedId);
        });

        whereToBuyButton.setOnClickListener(v -> {

            currencyConverterPresenter.onWhereToBuyButtonClicked();
        });
    }

    @Override
    public void populateBankSpinner(List<Bank> bankList) {

        bankSpinnerAdapter.clear();
        bankSpinnerAdapter.addItem(getActivity().getString(R.string.spinner_option_best_exchange), 0);
        bankSpinnerAdapter.addHeader(getActivity().getString(R.string.spinner_option_bank_list));
        for (Bank bank : bankList) {

            bankSpinnerAdapter.addItem(bank.getName(), bank.getId());
        }

        bankSpinner.setAdapter(bankSpinnerAdapter);
    }

    @Override
    public int getSelectedBankId() {

        BankSpinnerItem bankSpinnerItem = (BankSpinnerItem) bankSpinner.getSelectedItem();
        return bankSpinnerItem.getBankId();
    }

    @Override
    public void setBankSelection(int position) {

        bankSpinner.setSelection(position);
    }

    @Override
    public void setBankSelectionById(int bankId) {

        List<BankSpinnerItem> bankSpinnerItemList = bankSpinnerAdapter.getAllItems();
        int position = 0;
        for (BankSpinnerItem bankSpinnerItem : bankSpinnerItemList) {

            if (bankSpinnerItem.getBankId() == bankId)
                bankSpinner.setSelection(position);

            position++;
        }
    }

    @Override
    public void setBestExchangeBankText(String text) {

        bestExchangeBankField.setText(text);
    }

    @Override
    public void populateCurrencyInGroup(List<Currency> currencyList) {

        currenciesInGroup.addCurrencies(currencyList);
    }

    @Override
    public void populateCurrencyOutGroup(List<Currency> currencyList) {

        currenciesOutGroup.addCurrencies(currencyList);
    }

    @Override
    public int getCheckedCurrencyInId() {

        return currenciesInGroup.getCheckedRadioButtonId();
    }

    @Override
    public int getCheckedCurrencyOutId() {

        return currenciesOutGroup.getCheckedRadioButtonId();
    }

    @Override
    public void currencyInCheckNext(int checkedId) {

        currenciesInGroup.checkNextCurrency(checkedId);
    }

    @Override
    public void currencyOutCheckNext(int checkedId) {

        currenciesOutGroup.checkNextCurrency(checkedId);
    }

    @Override
    public void setCurrencyInCheckedById(int currencyId) {

        currenciesInGroup.setCurrencyCheckedById(currencyId);
    }

    @Override
    public void setCurrencyOutCheckedById(int currencyId) {

        currenciesOutGroup.setCurrencyCheckedById(currencyId);
    }

    @Override
    public void showNotificationToast(String message, NotificationToastType type) {

        ((ExchangeRatesActivity) getActivity()).showNotificationToast(message, type);
    }

    @Override
    public void showExchangeRatesView() {

        exchangeRatesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideExchangeRatesView() {

        exchangeRatesView.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {

        retryView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetryView() {

        retryView.setVisibility(View.GONE);
    }

    @Override
    public boolean onlyActiveNow() {
        return onlyActiveNowCheckBox.isChecked();
    }

    @Override
    public void showBranchesLayout() {

        branchesLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void populateBranchesList(List<Branch> branchList) {

        branchesListLayout.removeAllViews();

        for (Branch branch : branchList) {

            View v = getActivity().getLayoutInflater().inflate(R.layout.branches_list_item, null, false);
            TextView nameField = (TextView) v.findViewById(R.id.nameField);
            TextView addressField = (TextView) v.findViewById(R.id.addressField);

            nameField.setText(branch.getName());
            addressField.setText(String.format("Chișinău, %s, %s", branch.getAddress().getDistrict().getName(), branch.getAddress().getStreet()));

            branchesListLayout.addView(v);
        }
    }
}
