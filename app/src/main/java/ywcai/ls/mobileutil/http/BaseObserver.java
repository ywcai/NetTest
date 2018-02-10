package ywcai.ls.mobileutil.http;

import rx.Observer;

public abstract class BaseObserver<T> implements Observer<HttpBaseEntity<T>> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        onNetError(e);
    }

    @Override
    public void onNext(HttpBaseEntity<T> tHttpBaseEntity) {
        if (tHttpBaseEntity.isSuccess()) {
            success(tHttpBaseEntity.data);
        } else {
            onCodeError(tHttpBaseEntity.code, tHttpBaseEntity.msg);
        }
    }

    protected abstract void success(T t);

    protected abstract void onCodeError(int code, String msg);

    protected abstract void onNetError(Throwable e);
}
