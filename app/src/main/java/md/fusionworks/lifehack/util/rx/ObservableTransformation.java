package md.fusionworks.lifehack.util.rx;

import java.io.IOException;

import md.fusionworks.lifehack.data.api.exception.BadRequestException;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ObservableTransformation {

    public static final int RETRY_DELAY_MILLIS = 1000;
    public static final int MAX_RETRIES = 3;

    /**
     * {@link Observable.Transformer} that transforms the source observable to subscribe in the
     * io thread and observe on the Android's UI thread.
     */
    private static Observable.Transformer ioToMainThreadSchedulerTransformer;

    /**
     * {@link Observable.Transformer} that transform the source observable to handle api response
     * bad request that will emit bad exception error.
     */
    private static Observable.Transformer badRequestHandlerTransformer;

    /**
     * {@link Observable.Transformer} special transformer built for api request
     * that transform the source observable to apply next properties:
     * 1. Handle bad request exception and return an  error of {@link BadRequestException}.
     * 2. Retry on specific exception.
     * 3. Map api response "Response<T>" to T.
     */
    private static Observable.Transformer apiRequestConfigurationTransformer;

    static {

        ioToMainThreadSchedulerTransformer = createIOToMainThreadScheduler();
        badRequestHandlerTransformer = createBadRequestHandler();
        apiRequestConfigurationTransformer = createApiRequestConfigurationTransformer();
    }

    /**
     * Get {@link Observable.Transformer} that transforms the source observable to subscribe in
     * the io thread and observe on the Android's UI thread.
     * <p>
     * Because it doesn't interact with the emitted items it's safe ignore the unchecked casts.
     *
     * @return {@link Observable.Transformer}
     */
    @SuppressWarnings("unchecked")
    private static <T> Observable.Transformer<T, T> createIOToMainThreadScheduler() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * {@link Observable.Transformer} that transform the source observable to handle api response
     * bad request that will emit bad exception error.
     *
     * @return {@link Observable.Transformer}
     */
    @SuppressWarnings("unchecked")
    private static <T> Observable.Transformer<Response<T>, Response<T>> createBadRequestHandler() {

        return responseObservable -> responseObservable.flatMap(tResponse -> Observable.create((Observable.OnSubscribe<Response<T>>) subscriber -> {

            if (tResponse.isSuccess()) {

                subscriber.onNext(tResponse);
                subscriber.onCompleted();
            } else {

                int responseCode = tResponse.code();
                String errorMessage = "";
                try {
                    errorMessage = tResponse.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                subscriber.onError(new BadRequestException(errorMessage, responseCode));
            }
        }));
    }

    /**
     * {@link Observable.Transformer} special transformer built for api request
     * that transform the source observable to apply next properties:
     * 1. Handle bad request exception and return an  error of {@link BadRequestException}.
     * 2. Retry on specific exception.
     * 3. Map api response "Response<T>" to T.
     */
    @SuppressWarnings("unchecked")
    private static <T> Observable.Transformer<Response<T>, T> createApiRequestConfigurationTransformer() {

        return responseObservable -> responseObservable
                .flatMap(tResponse -> Observable.create((Observable.OnSubscribe<Response<T>>) subscriber -> {

                    if (tResponse.isSuccess()) {

                        subscriber.onNext(tResponse);
                        subscriber.onCompleted();
                    } else {

                        int responseCode = tResponse.code();
                        String errorMessage = "";
                        try {
                            errorMessage = tResponse.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        subscriber.onError(new BadRequestException(errorMessage, responseCode));
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
    public static <T> Observable.Transformer<Response<T>, Response<T>> applyBadRequestHandler() {

        return badRequestHandlerTransformer;
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<Response<T>, T> applyApiRequestConfiguration() {

        return apiRequestConfigurationTransformer;
    }
}