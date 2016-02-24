package md.fusionworks.lifehack.data.api.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ungvas on 1/23/16.
 */
public class HeaderInterceptor implements Interceptor {

  String apiKey;

  public HeaderInterceptor(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();
    Request request = original.newBuilder()
        .header("Accept", "application/json")
        .header("authorization", apiKey)
        .build();
    return chain.proceed(request);
  }
}
