package md.fusionworks.lifehack.data.api.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ungvas on 1/23/16.
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request = original.newBuilder()
                .header("Accept", "application/json")
                .header("authorization", "cb5fa2d6b00257fd769d2c68bf32c1a42ea0fd7c")
                .build();

        return chain.proceed(request);
    }
}
