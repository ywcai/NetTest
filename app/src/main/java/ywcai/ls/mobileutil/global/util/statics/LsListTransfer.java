package ywcai.ls.mobileutil.global.util.statics;


import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import ywcai.ls.mobileutil.tools.Sensor.model.SensorInfo;


public class LsListTransfer {


    public static String intToString(List<Integer> list, int start, int end) {
        if (start == end) {
            return "[ " + list.get(start) + " ]";
        }
        String temp = "[ ";
        for (int i = start; i < end; i++) {
            temp += list.get(i) + "";
            if (i < end - 1) {
                temp += ",";
            }
        }
        return temp + " ]";
    }

    public static String intToString(int[] ints, int start, int end) {
        if (start == end) {
            return "[ " + ints[start] + " ]";
        }
        String temp = "[ ";
        for (int i = start; i < end; i++) {
            temp += ints[i] + "";
            if (i < end - 1) {
                temp += ",";
            }
        }
        return temp + " ]";
    }

    public static String floatToString(float[] floats, int start, int end) {
        if (start == end) {
            return "[ " + floats[start] + " ]";
        }
        String temp = "[ ";
        for (int i = start; i < end; i++) {
            temp += (Math.round(floats[i] * 10)) / 10.0 + "";
            if (i < end - 1) {
                temp += ",";
            }
        }
        return temp + " ]";
    }


    public static String floatToString(List<Float> floats, int start, int end) {
        if (start == end) {
            return "[ " + floats.get(start) + " ]";
        }
        String temp = "[ ";
        for (int i = start; i < end; i++) {
            temp += (Math.round(floats.get(i) * 10)) / 10.0 + "";
            if (i < end - 1) {
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





