package md.fusionworks.lifehack.util.rx

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

object RxBusKotlin {

  private val bus = SerializedSubject(PublishSubject.create<Any>())

  fun postIfHasObservers(event: Any) {
    if (hasObservers()) bus.onNext(event)
  }

  fun post(event: Any) {
    bus.onNext(event)
  }

  fun asObservable(): Observable<Any> {

    return bus.asObservable()
  }

  fun <T> event(type: Class<T>): Observable<T> {
    return asObservable().ofType(type)
  }

  fun hasObservers(): Boolean {
    return bus.hasObservers()
  }
}