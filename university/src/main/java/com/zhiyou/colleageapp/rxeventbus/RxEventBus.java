package com.zhiyou.colleageapp.rxeventbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Create by LongWeiHu on 2016/5/30.
 */
public class RxEventBus {

    private final Subject<Object,Object> mSubjectBus;

    public RxEventBus() {
        mSubjectBus = new SerializedSubject<>(PublishSubject.create());
    }

    private static class SingletonFactory {
        private static volatile RxEventBus mDefaultRxEventBus = new RxEventBus();
    }

    public static RxEventBus getDefault() {
        return SingletonFactory.mDefaultRxEventBus;
    }


    public void postEvent(Object event) {
        mSubjectBus.onNext(event);
    }


    public Observable<Object> onReceiveEvent() {
        return mSubjectBus;
    }
}
