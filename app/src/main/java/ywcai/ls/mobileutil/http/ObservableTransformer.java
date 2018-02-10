package ywcai.ls.mobileutil.http;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zmy_11 on 2018/2/3.
 */

public class ObservableTransformer {
    public static Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object o) {
                return ((Observable) o).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
