package md.fusionworks.lifehack.ui.view;

/**
 * Created by ungvas on 10/22/15.
 */
public interface ExchangeRatesView extends View {

    void showLoading();

    void hideLoading();

    void showLoadingError();

    void setAmountInValue(String value);

    void setAmountOutValue(String value);

    void initializeViewListeners();
}
