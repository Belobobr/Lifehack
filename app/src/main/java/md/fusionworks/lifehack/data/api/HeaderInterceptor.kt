package md.fusionworks.lifehack.data.api

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by ungvas on 1/23/16.
 */
class HeaderInterceptor(internal var apiKey: String) : Interceptor {

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val original = chain.request()
    val request = original.newBuilder().header("Accept", "application/json").header("authorization",
        apiKey).build()
    return chain.proceed(request)
  }
}
