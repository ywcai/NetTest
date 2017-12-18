package ywcai.ls.mobileutil.global.util.statics;

import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;


public class ListToString {


    public static String IntegerToString(final List<Integer> list) {
        final String[] temp = new String[1];
        temp[0] = "[";
        Observable.from(list)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        temp[0] += integer;
                        if (list.indexOf(integer) < (list.size() - 1)) {
                            temp[0] += ",";
                        } else {
                            temp[0] += "]";
                        }
                    }
                });
        return temp[0];
    }
}





