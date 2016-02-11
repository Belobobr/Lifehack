package md.fusionworks.lifehack.util.rx;

import java.io.IOException;

import md.fusionworks.lifehack.data.api.ResponseCode;
import md.fusionworks.lifehack.data.api.exception.NotFoundException;
import md.fusionworks.lifehack.data.api.exception.UnknownException;
import md.fusionworks.lifehack.util.Constant;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ObservableTransformation {

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
                        if (tResponse.code() == ResponseCode.NOT_FOUND)
                            subscriber.onError(new NotFoundException());
                        else
                            subscriber.onError(new UnknownException());
                    }
                }))
                .retryWhen(new RetryWithDelayIf(Constant.MAX_RETRIES, Constant.RETRY_DELAY_MILLIS, throwable -> throwable instanceof IOException))
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