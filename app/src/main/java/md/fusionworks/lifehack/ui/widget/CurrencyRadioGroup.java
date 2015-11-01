package md.fusionworks.lifehack.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.model.Currency;

/**
 * Created by ungvas on 11/1/15.
 */
public class CurrencyRadioGroup extends RadioGroup {

    public CurrencyRadioGroup(Context context) {
        super(context);
    }

    public CurrencyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addItems(List<Currency> currencyList) {

        for (Currency currency : currencyList) {

            addItem(currency);
        }
    }

    public void addItem(Currency currency) {

        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 20f);
layoutParams.setMarginStart(getContext().getResources().getDimensionPixelOffset(R.dimen.currency_radio_button_margin));

        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setLayoutParams(layoutParams);
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setId(currency.getId());
        radioButton.setText(currency.getName());
        radioButton.setButtonDrawable(android.R.color.transparent);
        radioButton.setTextColor(getContext().getResources().getColor(R.color.white));
        radioButton.setBackgroundResource(R.drawable.bg_currency_radio_button);
        addView(radioButton);
    }

    public void setItemCheckedByIndex(int index) {

        int itemsCount = getChildCount();
        if (itemsCount >= index) {

            RadioButton radioButton = (RadioButton) getChildAt(index);
            radioButton.setChecked(true);
        }
    }
}
