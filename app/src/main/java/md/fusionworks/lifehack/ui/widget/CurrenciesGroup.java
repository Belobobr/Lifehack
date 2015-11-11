package md.fusionworks.lifehack.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.entity.Currency;

/**
 * Created by ungvas on 11/1/15.
 */
public class CurrenciesGroup extends RadioGroup {

    public CurrenciesGroup(Context context) {
        super(context);
    }

    public CurrenciesGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addCurrencies(List<Currency> currencyList) {

        for (Currency currency : currencyList) {

            addCurrency(currency);
        }
    }

    public void addCurrency(Currency currency) {

        int padding = getResources().getDimensionPixelOffset(R.dimen.element_spacing_normal);
        int marginLeft = getResources().getDimensionPixelOffset(R.dimen.currency_radio_button_margin);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 20f);
        layoutParams.setMargins(marginLeft, 0, 0, 0);

        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setLayoutParams(layoutParams);
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setId(currency.getId());
        radioButton.setPadding(padding, padding, padding, padding);
        radioButton.setText(currency.getName());
        radioButton.setButtonDrawable(android.R.color.transparent);
        radioButton.setTextColor(getResources().getColor(R.color.white));
        radioButton.setBackgroundResource(R.drawable.bg_currency_radio_button);
        addView(radioButton);
    }

    public void setCurrencyCheckedByIndex(int index) {

        int itemsCount = getChildCount();
        if (itemsCount >= index) {

            RadioButton radioButton = (RadioButton) getChildAt(index);
            radioButton.setChecked(true);
        }
    }

    public void setCurrencyCheckedById(int currencyId) {

        RadioButton radioButton = (RadioButton) findViewById(currencyId);
        radioButton.setChecked(true);
    }

    public void checkNextCurrency(int checkedId) {

        View radioButton = findViewById(checkedId);
        int index = indexOfChild(radioButton);
        setCurrencyCheckedByIndex(getNextCurrencyIndex(index));
    }

    private int getNextCurrencyIndex(int index) {

        int itemsCount = getChildCount();
        if (index < itemsCount - 1)
            return index + 1;

        else return 0;
    }
}
