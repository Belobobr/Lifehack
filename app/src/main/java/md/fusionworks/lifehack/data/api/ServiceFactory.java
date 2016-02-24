package md.fusionworks.lifehack.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import md.fusionworks.lifehack.BuildConfig;
import md.fusionworks.lifehack.data.api.interceptor.HeaderInterceptor;
import md.fusionworks.lifehack.util.Constant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

  public static BanksService buildBanksService() {

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    httpLoggingInterceptor.setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
        .addInterceptor(new HeaderInterceptor("cb5fa2d6b00257fd769d2c68bf32c1a42ea0fd7c"))
        .connectTimeout(Constant.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(Constant.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .build();

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    Retrofit retrofit = new Retrofit.Builder().baseUrl(BanksService.ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(BanksService.class);
  }

  public static PricesService buildPricesService() {

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    httpLoggingInterceptor.setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
        .addInterceptor(new HeaderInterceptor("zEjoa61qZS"))
        .connectTimeout(Constant.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(Constant.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .build();

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    Retrofit retrofit = new Retrofit.Builder().baseUrl(PricesService.ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(PricesService.class);
  }
}