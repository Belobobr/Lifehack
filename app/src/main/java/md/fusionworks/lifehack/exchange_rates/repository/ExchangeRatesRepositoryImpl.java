package md.fusionworks.lifehack.exchange_rates.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import md.fusionworks.lifehack.data.api.Callback;
import md.fusionworks.lifehack.data.api.LifehackClient;
import md.fusionworks.lifehack.data.api.ServiceCreator;
import md.fusionworks.lifehack.data.api.exception.HttpResponseException;
import md.fusionworks.lifehack.data.api.exception.NetworkConnectionException;
import md.fusionworks.lifehack.data.api.model.Bank;
import md.fusionworks.lifehack.data.api.model.Branch;
import md.fusionworks.lifehack.data.api.model.Currency;
import md.fusionworks.lifehack.data.api.model.Rate;
import md.fusionworks.lifehack.util.NetworkUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ungvas on 11/3/15.
 */

public class ExchangeRatesRepositoryImpl implements ExchangeRatesRepository {

    private Context context;
    private LifehackClient lifehackClient;


    public ExchangeRatesRepositoryImpl(Context context) {

        this.context = context;
        lifehackClient = ServiceCreator.createService(LifehackClient.class, LifehackClient.BASE_URL, "cb5fa2d6b00257fd769d2c68bf32c1a42ea0fd7c");
    }

    @Override
    public void getBanks(@NonNull Callback<List<Bank>> callback) {

        if (NetworkUtils.isThereInternetConnection(context)) {

            Call<List<Bank>> call = lifehackClient.getBanks();
            call.enqueue(new retrofit2.Callback<List<Bank>>() {

                @Override
                public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                    if (response.isSuccess()) {

                        callback.onSuccess(response.body());
                    } else {

                        callback.onError(new HttpResponseException());
                    }
                }

                @Override
                public void onFailure(Call<List<Bank>> call, Throwable t) {

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
            call.enqueue(new retrofit2.Callback<List<Currency>>() {

                @Override
                public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                    if (response.isSuccess()) {

                        callback.onSuccess(response.body());
                    } else {

                        callback.onError(new HttpResponseException());
                    }
                }

                @Override
                public void onFailure(Call<List<Currency>> call, Throwable t) {
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
            call.enqueue(new retrofit2.Callback<List<Rate>>() {

                @Override
                public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                    if (response.isSuccess()) {

                        callback.onSuccess(response.body());
                    } else {

                        callback.onError(new HttpResponseException());
                    }
                }

                @Override
                public void onFailure(Call<List<Rate>> call, Throwable t) {

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
            call.enqueue(new retrofit2.Callback<List<Branch>>() {

                @Override
                public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                    if (response.isSuccess()) {

                        callback.onSuccess(response.body());
                    } else {

                        callback.onError(new HttpResponseException());
                    }
                }

                @Override
                public void onFailure(Call<List<Branch>> call, Throwable t) {
                    callback.onError(new NetworkConnectionException());
                }
            });
        } else
            callback.onError(new NetworkConnectionException());
    }
}
