package md.fusionworks.lifehack.util.rx;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

  private static RxBus instance;

  private RxBus() {
  }

  public static RxBus getInstance() {
    if (null == instance) {
      instance = new RxBus();
    }
    return instance;
  }

  private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

  public void postIfHasObservers(Object event) {
    if (hasObservers()) bus.onNext(event);
  }

  public void post(Object event) {
    bus.onNext(event);
  }

  public Observable<Object> asObservable() {

    return bus.asObservable();
  }

  public <T> Observable<T> event(Class<T> type) {

    return asObservable().ofType(type);
  }

  public boolean hasObservers() {
    return bus.hasObservers();
  }
}