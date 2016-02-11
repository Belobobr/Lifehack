package md.fusionworks.lifehack.util.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

public class RetryWithDelayIf implements Func1<Observable<? extends Throwable>, Observable<?>> {

  private final int maxRetries;
  private final int retryDelayMillis;
  private int retryCount;
  private Func1<Throwable, Boolean> retryIf;

  public RetryWithDelayIf(final int maxRetries, final int retryDelayMillis,
      Func1<Throwable, Boolean> retryIf) {
    this.maxRetries = maxRetries;
    this.retryDelayMillis = retryDelayMillis;
    this.retryCount = 0;
    this.retryIf = retryIf;
  }

  @Override public Observable<?> call(Observable<? extends Throwable> attempts) {
    return attempts.flatMap(new Func1<Throwable, Observable<?>>() {
      @Override public Observable<?> call(Throwable throwable) {

        if (retryIf.call(throwable) && ++retryCount < maxRetries) {
          // When this Observable calls onNext, the original
          // Observable will be retried (i.e. re-subscribed).
          return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
        }

        // Max retries hit. Just pass the error along.
        return Observable.error(throwable);
      }
    });
  }
}