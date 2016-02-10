package md.fusionworks.lifehack.util.rx;

import rx.Observer;

/**
 * Observer adapter that allow you to omit not needed method implementations.
 * @param <T>
 */
public abstract class ObserverAdapter<T> implements Observer<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }
}