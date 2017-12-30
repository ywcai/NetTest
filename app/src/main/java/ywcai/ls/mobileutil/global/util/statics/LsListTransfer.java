package ywcai.ls.mobileutil.global.util.statics;


import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import ywcai.ls.mobileutil.tools.Sensor.model.SensorInfo;


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

    public static String floatString(float[] floats) {
        String temp = "[ ";
        for (int i = 0; i < floats.length; i++) {
            temp += (Math.round(floats[i] * 10)) / 10.0 + "";
            if (i < floats.length - 1) {
                temp += ",";
            }
            if (i >= 3) {
                break;
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

    public static int[] copyInts(int[] ints) {
        int[] temp = new int[ints.length];
        for (int i = 0; i < ints.length; i++) {
            temp[i] = ints[i];
        }
        return temp;
    }


    public static int getIndexWithInteger(List<Integer> list, int find) {
        int pos = -1;
        if (list == null) {
            return pos;
        }
        if (list.size() == 0) {
            return pos;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(find)) {
                return i;
            }
        }
        return pos;
    }

    public static int getIndexWithString(List<String> list, String find) {
        int pos = -1;
        if (list == null) {
            return pos;
        }
        if (list.size() == 0) {
            return pos;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(find)) {
                return i;
            }
        }
        return pos;
    }

    public static int getIndexWithSensorName(List<SensorInfo> list, String find) {
        int pos = -1;
        if (list == null) {
            return pos;
        }
        if (list.size() == 0) {
            return pos;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).englishName.equals(find)) {
                return i;
            }
        }
        return pos;
    }


}





