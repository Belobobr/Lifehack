package md.fusionworks.lifehack.util.rx;

import java.io.IOException;

import md.fusionworks.lifehack.data.api.exception.NotFoundException;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ObservableTransformation {
    public static final int RETRY_DELAY_MILLIS = 1000;
    public static final int MAX_RETRIES = 3;

    private static Observable.Transformer ioToMainThreadSchedulerTransformer;
    private static Observable.Transformer apiRequestConfigurationTransformer;

    static {
        ioToMainThreadSchedulerTransformer = createIOToMainThreadScheduler();
        apiRequestConfigurationTransformer = createApiRequestConfigurationTransformer();
    }

    @SuppressWarnings("unchecked")
    private static <T> Observable.Transformer<T, T> createIOToMainThreadScheduler() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("unchecked")
    private static <T> Observable.Transformer<Response<T>, T> createApiRequestConfigurationTransformer() {
        return responseObservable -> responseObservable
                .flatMap(tResponse -> Observable.create((Observable.OnSubscribe<Response<T>>) subscriber -> {
                    if (tResponse.isSuccess()) {
                        subscriber.onNext(tResponse);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new NotFoundException());
                    }
                }))
                .retryWhen(new RetryWithDelayIf(MAX_RETRIES, RETRY_DELAY_MILLIS, throwable -> throwable instanceof IOException))
                .map(t -> t.body());
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applyIOToMainThreadSchedulers() {
        return ioToMainThreadSchedulerTransformer;
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<Response<T>, T> applyApiRequestConfiguration() {
        return apiRequestConfigurationTransformer;
    }
}