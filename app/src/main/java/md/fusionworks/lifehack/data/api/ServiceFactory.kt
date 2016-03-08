package md.fusionworks.lifehack.data.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import md.fusionworks.lifehack.BuildConfig
import md.fusionworks.lifehack.util.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceFactory {
  fun buildBanksService(): BanksService {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).addInterceptor(
        HeaderInterceptor("cb5fa2d6b00257fd769d2c68bf32c1a42ea0fd7c")).connectTimeout(
        Constant.CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS).readTimeout(
        Constant.CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS).addNetworkInterceptor(
        StethoInterceptor()).build()

    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

    val retrofit = Retrofit.Builder().baseUrl(BanksService.ENDPOINT).client(
        okHttpClient).addConverterFactory(GsonConverterFactory.create(gson)).addCallAdapterFactory(
        RxJavaCallAdapterFactory.create()).build()

    return retrofit.create(BanksService::class.java)
  }

  fun buildPricesService(): PricesService {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).addInterceptor(
        HeaderInterceptor("zEjoa61qZS")).connectTimeout(Constant.CONNECTION_TIMEOUT.toLong(),
        TimeUnit.SECONDS).readTimeout(Constant.CONNECTION_TIMEOUT.toLong(),
        TimeUnit.SECONDS).addNetworkInterceptor(StethoInterceptor()).build()

    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

    val retrofit = Retrofit.Builder().baseUrl(PricesService.ENDPOINT).client(
        okHttpClient).addConverterFactory(GsonConverterFactory.create(gson)).addCallAdapterFactory(
        RxJavaCallAdapterFactory.create()).build()

    return retrofit.create(PricesService::class.java)
  }
}