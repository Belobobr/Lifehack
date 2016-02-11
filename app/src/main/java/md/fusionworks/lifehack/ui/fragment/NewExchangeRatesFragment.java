package md.fusionworks.lifehack.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.Bind;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.widget.CurrenciesGroup;
import md.fusionworks.lifehack.ui.widget.DateView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewExchangeRatesFragment extends BaseFragment {

  @Bind(R.id.amountInField) EditText amountInField;
  @Bind(R.id.amountOutField) EditText amountOutField;
  @Bind(R.id.bankSpinner) Spinner bankSpinner;
  @Bind(R.id.bestExchangeBankField) TextView bestExchangeBankField;
  @Bind(R.id.ratesDateField) DateView ratesDateField;
  @Bind(R.id.currencyInRadioGroup) CurrenciesGroup currenciesInGroup;
  @Bind(R.id.currencyOutRadioGroup) CurrenciesGroup currenciesOutGroup;
  @Bind(R.id.exchangeRatesView) LinearLayout exchangeRatesView;
  @Bind(R.id.retryView) RelativeLayout retryView;
  @Bind(R.id.retryButton) Button retryButton;
  @Bind(R.id.whereToBuyButton) TextView whereToBuyButton;
  @Bind(R.id.onlyActiveNowCheckBox) CheckBox onlyActiveNowCheckBox;

  public NewExchangeRatesFragment() {
    // Required empty public constructor
  }

  public static NewExchangeRatesFragment newInstance() {
    return new NewExchangeRatesFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflateAndBindViews(inflater, R.layout.fragment_new_exchange_rates, container);
  }
}
