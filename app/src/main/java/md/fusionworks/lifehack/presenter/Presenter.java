package md.fusionworks.lifehack.presenter;

import android.support.annotation.NonNull;

/**
 * Created by ungvas on 10/16/15.
 */
public interface Presenter<T> {

    void attachView(@NonNull T view);

    void detachView(@NonNull T view);

    void destroy();
}
