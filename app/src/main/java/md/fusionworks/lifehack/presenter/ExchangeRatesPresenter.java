package md.fusionworks.lifehack.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.di.scope.PerFragment;
import md.fusionworks.lifehack.ui.view.ExchangeRatesView;

/**
 * Created by ungvas on 10/22/15.
 */
@PerActivity
public class ExchangeRatesPresenter implements Presenter<ExchangeRatesView> {

    private Context context;
    private ExchangeRatesView exchangeRatesView;

    @Inject
    public ExchangeRatesPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(@NonNull ExchangeRatesView view) {

        exchangeRatesView = view;
    }

    @Override
    public void detachView(@NonNull ExchangeRatesView view) {

        exchangeRatesView = null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
