package md.fusionworks.lifehack.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.adapter.BankSpinnerAdapter;
import md.fusionworks.lifehack.di.component.ExchangeRatesComponent;
import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.BankSpinnerItem;
import md.fusionworks.lifehack.model.Currency;
import md.fusionworks.lifehack.presenter.ExchangeRatesPresenter;
import md.fusionworks.lifehack.ui.view.ExchangeRatesView;
import md.fusionworks.lifehack.ui.widget.CurrencyRadioGroup;
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
    CurrencyRadioGroup currencyInRadioGroup;
    @Bind(R.id.currencyOutRadioGroup)
    CurrencyRadioGroup currencyOutRadioGroup;

    @Inject
    ExchangeRatesPresenter exchangeRatesPresenter;
    private MaterialDialog loadingRatesDialog;
    private BankSpinnerAdapter bankSpinnerAdapter;

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
        setupUI();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
    }

    private void initialize() {

        getComponent(ExchangeRatesComponent.class).inject(this);
        exchangeRatesPresenter.attachView(this);
        exchangeRatesPresenter.initialize();
    }

    private void setupUI() {

        bankSpinnerAdapter = new BankSpinnerAdapter(getActivity());
    }

    @Override
    public void showLoading() {

        if (loadingRatesDialog == null) {
            loadingRatesDialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.field_loading_rates)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(true)
                    .show();
        }

        if (!loadingRatesDialog.isShowing())
            loadingRatesDialog.show();
    }

    @Override
    public void hideLoading() {

        if (loadingRatesDialog != null)
            if (loadingRatesDialog.isShowing())
                loadingRatesDialog.hide();

        loadingRatesDialog = null;
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void setAmountInValue(String value) {

        amountInField.setText(value);
    }

    @Override
    public String getAmountInValue() {

        return amountInField.getText().toString();
    }

    @Override
    public void setAmountOutValue(String value) {

        amountOutField.setText(value);
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

                exchangeRatesPresenter.convert();
            }
        });

        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                exchangeRatesPresenter.convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ratesDateField.setOnDateChangedListener(date -> {

            exchangeRatesPresenter.onRatesDateChanged(date);
        });

        currencyInRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            exchangeRatesPresenter.convert();
        });


        currencyOutRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            exchangeRatesPresenter.convert();
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
    public void setBestExchangeBankText(String text) {

        bestExchangeBankField.setText(text);
    }

    @Override
    public void populateCurrencyInRadioGroup(List<Currency> currencyList) {

        currencyInRadioGroup.addItems(currencyList);
    }

    @Override
    public void populateCurrencyOutRadioGroup(List<Currency> currencyList) {

        currencyOutRadioGroup.addItems(currencyList);
    }

    @Override
    public void clearCurrencyInRadioGroupItems() {

        currencyInRadioGroup.removeAllViews();
    }

    @Override
    public void clearCurrencyOutRadioGroupItems() {

        currencyInRadioGroup.removeAllViews();
    }

    @Override
    public int getCheckedCurrencyInId() {

        return currencyInRadioGroup.getCheckedRadioButtonId();
    }

    @Override
    public int getCheckedCurrencyOutId() {

        return currencyOutRadioGroup.getCheckedRadioButtonId();
    }

    @Override
    public void setCurrencyInRadioGroupItemChecked(int index) {

        currencyInRadioGroup.setItemCheckedByIndex(index);
    }

    @Override
    public void setCurrencyOutRadioGroupChecked(int index) {

        currencyOutRadioGroup.setItemCheckedByIndex(index);
    }
}
