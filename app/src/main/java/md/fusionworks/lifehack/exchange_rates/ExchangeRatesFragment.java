package md.fusionworks.lifehack.exchange_rates;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
public class ExchangeRatesFragment extends BaseFragment implements ExchangeRatesView {

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

    ExchangeRatesPresenter exchangeRatesPresenter;
    BankSpinnerAdapter bankSpinnerAdapter;

    private MaterialDialog loadingInitialDataDialog;
    private MaterialDialog loadingRatesErrorDialog;

    public static ExchangeRatesFragment newInstance() {

        return new ExchangeRatesFragment();
    }

    public ExchangeRatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_exchange_rates, container, false);
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

        exchangeRatesPresenter = new ExchangeRatesPresenter(getActivity());
        exchangeRatesPresenter.attachView(this);
        exchangeRatesPresenter.initialize();
    }

    private void setupUI() {

        bankSpinnerAdapter = new BankSpinnerAdapter(getActivity());
        retryButton.setOnClickListener(v -> {

            exchangeRatesPresenter.onRetryButtonClicked();
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

                        exchangeRatesPresenter.onLoadingRatesErrorCancel();
                    })
                    .onPositive((materialDialog, dialogAction) -> exchangeRatesPresenter.onLoadingRatesErrorTryAgain(date))
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

                exchangeRatesPresenter.afterAmountInTextChanged(s.toString());
            }
        });

        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                exchangeRatesPresenter.onBankSelected(position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ratesDateField.setOnDateChangedListener(date -> {

            exchangeRatesPresenter.onRatesDateChanged(date);
        });

        currenciesInGroup.setOnCheckedChangeListener((group, checkedId) -> {

            exchangeRatesPresenter.onCurrencyInChanged(group, checkedId);
        });


        currenciesOutGroup.setOnCheckedChangeListener((group, checkedId) -> {

            exchangeRatesPresenter.onCurrencyOutChanged(group, checkedId);
        });

        whereToBuyButton.setOnClickListener(v -> {

            exchangeRatesPresenter.onWhereToBuyButtonClicked();
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

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        branchesListLayout.removeAllViews();

        for (Branch branch : branchList) {

            View v = layoutInflater.inflate(R.layout.branches_list_item, null, false);
            TextView nameField = (TextView) v.findViewById(R.id.nameField);
            TextView addressField = (TextView) v.findViewById(R.id.addressField);

            nameField.setText(branch.getName());
            addressField.setText(getBranchAddress(branch));

            branchesListLayout.addView(v);
        }
    }

    private boolean isBranchDetailEmpty(String detail) {

        if (TextUtils.isEmpty(detail) || detail.equals("-"))
            return true;

        return false;
    }

    private String getBranchAddress(Branch branch) {

        String address = "";
        String locality = branch.getAddress().getDistrict().getLocality().getName();
        String district = branch.getAddress().getDistrict().getName();
        String street = branch.getAddress().getStreet();
        String house = branch.getAddress().getHouse();
        String phone = branch.getAddress().getPhone();

        if (!isBranchDetailEmpty(locality))
            address += locality;
        if (!isBranchDetailEmpty(district))
            address += " ," + district;
        if (!isBranchDetailEmpty(street) || !isBranchDetailEmpty(house))
            address += " ," + street + " " + house;
        if (!isBranchDetailEmpty(phone))
            address += "\n" + phone;

        return address;
    }
}
