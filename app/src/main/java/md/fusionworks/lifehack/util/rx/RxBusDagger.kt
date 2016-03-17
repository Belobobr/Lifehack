package md.fusionworks.lifehack.util.rx

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

/**
 * Created by ungvas on 3/17/16.
 */
class RxBusDagger {
  private val bus = SerializedSubject(PublishSubject.create<Any>())

  fun postIfHasObservers(event: Any) {
    if (hasObservers()) bus.onNext(event)
  }

  fun post(event: Any) = bus.onNext(event)

  fun asObservable(): Observable<Any> = bus.asObservable()

  fun <T> event(type: Class<T>): Observable<T> = asObservable().ofType(type)

  fun hasObservers(): Boolean = bus.hasObservers()
}