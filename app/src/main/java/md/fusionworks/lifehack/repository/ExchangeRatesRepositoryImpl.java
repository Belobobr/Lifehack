package md.fusionworks.lifehack.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import md.fusionworks.lifehack.data.exception.HttpResponseException;
import md.fusionworks.lifehack.data.exception.NetworkConnectionException;
import md.fusionworks.lifehack.data.net.Callback;
import md.fusionworks.lifehack.data.net.LifehackClient;
import md.fusionworks.lifehack.data.net.ServiceCreator;
import md.fusionworks.lifehack.di.scope.PerActivity;
import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.Branch;
import md.fusionworks.lifehack.model.Currency;
import md.fusionworks.lifehack.model.Rate;
import md.fusionworks.lifehack.util.NetworkUtils;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ungvas on 11/3/15.
 */

@Singleton
public class ExchangeRatesRepositoryImpl implements ExchangeRatesRepository {

    private Context context;
    private LifehackClient lifehackClient;

    @Inject
    public ExchangeRatesRepositoryImpl(@Named("application") Context context) {

        this.context = context;
        lifehackClient = ServiceCreator.createService(LifehackClient.class, LifehackClient.BASE_URL, "cb5fa2d6b00257fd769d2c68bf32c1a42ea0fd7c");
    }

    @Override
    public void getBanks(@NonNull Callback<List<Bank>> callback) {

        if (NetworkUtils.isThereInternetConnection(context)) {

            Call<List<Bank>> call = lifehackClient.getBanks();
            call.enqueue(new retrofit.Callback<List<Bank>>() {
                @Override
                public void onResponse(Response<List<Bank>> response, Retrofit retrofit) {

                    if (response.isSuccess()) {

                        callback.onSuccess(response.body());
                    } else {

                        callback.onError(new HttpResponseException());
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                    callback.onError(new NetworkConnectionException());
                }
            });
        } else
            callback.onError(new NetworkConnectionException());
    }

    @Override
    public void getCurrencies(@NonNull Callback<List<Currency>> callback) {

        if (NetworkUtils.isThereInternetConnection(context)) {

            Call<List<Currency>> call = lifehackClient.getCurrencies();
            call.enqueue(new retrofit.Callback<List<Currency>>() {
                @Override
                public void onResponse(Response<List<Currency>> response, Retrofit retrofit) {

                    if (response.isSuccess()) {

                        callback.onSuccess(response.body());
                    } else {

                        callback.onError(new HttpResponseException());
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                    callback.onError(new NetworkConnectionException());
                }
            });
        } else
            callback.onError(new NetworkConnectionException());
    }

    @Override
    public void getRates(String date, @NonNull Callback<List<Rate>> callback) {

        if (NetworkUtils.isThereInternetConnection(context)) {

            Call<List<Rate>> call = lifehackClient.getRates(date);
            call.enqueue(new retrofit.Callback<List<Rate>>() {
                @Override
                public void onResponse(Response<List<Rate>> response, Retrofit retrofit) {

                    if (response.isSuccess()) {

                        callback.onSuccess(response.body());
                    } else {

                        callback.onError(new HttpResponseException());
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                    callback.onError(new NetworkConnectionException());
                }
            });
        } else
            callback.onError(new NetworkConnectionException());
    }

    @Override
    public void getBankBranches(int bankId, boolean active, @NonNull Callback<List<Branch>> callback) {

        if (NetworkUtils.isThereInternetConnection(context)) {

            Call<List<Branch>> call = lifehackClient.getBankBranches(bankId, active);
            call.enqueue(new retrofit.Callback<List<Branch>>() {
                @Override
                public void onResponse(Response<List<Branch>> response, Retrofit retrofit) {

                    if (response.isSuccess()) {

                        callback.onSuccess(response.body());
                    } else {

                        callback.onError(new HttpResponseException());
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                    callback.onError(new NetworkConnectionException());
                }
            });
        } else
            callback.onError(new NetworkConnectionException());
    }
}
