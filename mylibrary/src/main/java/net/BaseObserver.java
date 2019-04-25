package net;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author dong
 * @param <T>
 */

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        try {
            onCustomNext(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {


        RxExceptionUtil.exceptionHandler(e);

    }

    @Override
    public void onComplete() {

    }
    protected abstract void onCustomNext(T o) throws Exception;
}
