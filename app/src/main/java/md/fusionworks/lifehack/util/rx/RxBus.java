package md.fusionworks.lifehack.util.rx;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

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