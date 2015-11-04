package md.fusionworks.lifehack.ui.fragment;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

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

    @Inject ExchangeRatesPresenter exchangeRatesPresenter;
    @Inject BankSpinnerAdapter bankSpinnerAdapter;

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

        getComponent(ExchangeRatesComponent.class).inject(this);
        exchangeRatesPresenter.attachView(this);
        exchangeRatesPresenter.initialize();
    }

    private void setupUI() {

        retryButton.setOnClickListener(v -> {

            exchangeRatesPresenter.onRetryButtonClicked();
        });
    }

    @Override
    public void showLoadingInitialData() {

        if (loadingInitialDataDialog == null) {
            loadingInitialDataDialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.field_loading_rates)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(true)
                    .show();
        }

        if (!loadingInitialDataDialog.isShowing())
            loadingInitialDataDialog.show();
    }

    @Override
    public void hideLoadingInitialData() {

        if (loadingInitialDataDialog != null)
            if (loadingInitialDataDialog.isShowing())
                loadingInitialDataDialog.hide();

        loadingInitialDataDialog = null;
    }

    @Override
    public void showLoadingRatesError(String date) {

        if (loadingRatesErrorDialog == null) {

            loadingRatesErrorDialog = new MaterialDialog.Builder(getActivity())
                    .content("Не удалось загрузить курс. Проверьте интернет соединение.")
                    .positiveText("Попробовать снова")
                    .negativeText("Отменить")
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

        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);

        switch (type) {

            case INFO:
                break;
            case ERROR:

                View view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                break;
        }

        snackbar.show();
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
}
