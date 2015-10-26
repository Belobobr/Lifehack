package md.fusionworks.lifehack.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import md.fusionworks.lifehack.R;

/**
 * Created by ungvas on 10/23/15.
 */
public class CurrencyView extends TypefaceTextView {

    private static final int BG_CURRENCY_VIEW_STATE_NORMAL = R.drawable.bg_currency_view_state_normal;
    private static final int BG_CURRENCY_VIEW_STATE_SELECTED = R.drawable.bg_currency_view_state_selected;

    private int bgCurrencyViewResId;

    public CurrencyView(Context context) {
        super(context, null, R.attr.currencyViewStyle);

        initialize();
    }

    public CurrencyView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.currencyViewStyle);

        initialize();
    }

    private void initialize() {

        setSelected(false);
    }

    public void setSelected(boolean selected) {

        setBackgroundResource(selected ? BG_CURRENCY_VIEW_STATE_SELECTED : BG_CURRENCY_VIEW_STATE_NORMAL);
    }

    public boolean isSelected() {

        return bgCurrencyViewResId == BG_CURRENCY_VIEW_STATE_SELECTED;
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);

        bgCurrencyViewResId = resid;
    }
}
