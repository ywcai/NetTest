package ywcai.ls.mobileutil.global.util.statics;


import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


public class LsListTransfer {


    public static String IntegerToString(final List<Integer> list) {
        final String[] temp = new String[1];
        temp[0] = "[ ";
        Observable.from(list)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        temp[0] += integer;
                        if (list.indexOf(integer) < (list.size() - 1)) {
                            temp[0] += ",";
                        }
                    }
                });
        return temp[0] + " ]";
    }

    public static String intToString(int[] ints) {
        String temp = "[ ";

        for (int i = 0; i < ints.length; i++) {
            temp += ints[i] + "";
            if (i < ints.length - 1) {
                temp += ",";
            }
        }
        return temp + " ]";
    }

    public static boolean isHasInteger(List<Integer> list, Integer number) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).intValue() == number.intValue()) {
                return true;
            }
        }
        return false;
    }

    public static int count(int[] ints) {
        int result = 0;
        for (int i = 0; i < ints.length; i++) {
            result += ints[i];
        }
        return result;
    }
}





