package md.fusionworks.lifehack.rx

import rx.Observer

/**
 * Observer adapter that allow you to omit not needed method implementations.
 */
abstract class ObserverAdapter<T> : Observer<T> {
  override fun onCompleted() {

  }

  override fun onError(e: Throwable) {

  }

  override fun onNext(t: T) {

  }
}