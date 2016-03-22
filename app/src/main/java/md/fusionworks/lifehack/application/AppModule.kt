package md.fusionworks.lifehack.application

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.realm.Realm
import md.fusionworks.lifehack.BuildConfig
import md.fusionworks.lifehack.api.banks.CinemaService
import md.fusionworks.lifehack.navigator.Navigator
import md.fusionworks.lifehack.rx.RxBusDagger
import md.fusionworks.lifehack.util.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by ungvas on 3/17/16.
 */
@Module
class AppModule(application: Application) {

  @Provides
  @Singleton
  fun provideRxBus() = RxBusDagger()

  @Provides
  @Singleton
  fun provideRealm() = Realm.getDefaultInstance()

  @Provides
  @Singleton
  fun provideNavigator() = Navigator()

  @Provides
  @Singleton
  fun provideGsonConverter() = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

  @Provides
  @Singleton
  fun provideCinemaService(gson: Gson): CinemaService {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(Constant.CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(Constant.CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(CinemaService.ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(
            RxJavaCallAdapterFactory.create())
        .build()

    return retrofit.create(CinemaService::class.java)
  }
}