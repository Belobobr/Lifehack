package md.fusionworks.lifehack.util.rx

import md.fusionworks.lifehack.data.api.NotFoundException
import md.fusionworks.lifehack.data.api.ResponseCode
import md.fusionworks.lifehack.data.api.UnknownException
import md.fusionworks.lifehack.util.Constant
import retrofit2.Response
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException

object ObservableTransformation {

  @SuppressWarnings("unchecked")
  fun <T> applyIOToMainThreadSchedulers(): Observable.Transformer<T, T> = Observable.Transformer<T, T> { tObservable ->
    tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
  }

  @SuppressWarnings("unchecked")
  fun  <T> applyApiRequestConfiguration(): Observable.Transformer<Response<T>, T> {
    return Observable.Transformer<Response<T>, T> {
      it.flatMap { response ->
        Observable.create<Subscriber<Response<T>>> { it ->
          Observable.OnSubscribe<Response<T>> {
            if (response.isSuccessful) {
              it.onNext(response)
              it.onCompleted()
            } else {
              if (response.code() == ResponseCode.NOT_FOUND) {
                it.onError(NotFoundException())
              } else {
                it.onError(UnknownException())
              }
            }
          }
        }
      }
      it.retryWhen(
          RetryWithDelayIf(Constant.MAX_RETRIES, Constant.RETRY_DELAY_MILLIS) { it is IOException })
          .map { it.body() }
    }
  }
}