package md.fusionworks.lifehack.data.net;

import java.util.List;

/**
 * Created by ungvas on 11/3/15.
 */
public interface Callback<T> {

    void onSuccess(T response);

    void onError(Throwable t);
}
