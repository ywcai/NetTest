package ywcai.ls.mobileutil.global.util.statics;


import java.util.List;


import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.tools.Sensor.model.SensorInfo;


public class LsListTransfer {


    public static String intToString(List<Integer> list) {
        if (list == null) {
            return "[ ]";
        }
        return intToString(list, 0, list.size(), "");
    }

    public static String intToString(int[] ints) {
        if (ints == null) {
            return "[ ]";
        }
        return intToString(ints, 0, ints.length, "");
    }

    public static String intToString(List<Integer> list, int start, int end) {
        if (list == null) {
            return "[ ]";
        }
        return intToString(list, start, end, "");
    }

    public static String intToString(int[] ints, int start, int end) {
        if (ints == null) {
            return "[ ]";
        }
        return intToString(ints, start, end, "");
    }

    public static String intToString(List<Integer> list, String unit) {
        if (list == null) {
            return "[ ]";
        }
        return intToString(list, 0, list.size(), unit);
    }

    public static String intToString(int[] ints, String unit) {
        if (ints == null) {
            return "[ ]";
        }
        return intToString(ints, 0, ints.length, unit);
    }


    public static String floatToString(List<Float> floats) {
        if (floats == null) {
            return "[ ]";
        }
        return floatToString(floats, 0, floats.size(), "");
    }

    public static String floatToString(float[] floats) {
        if (floats == null) {
            return "[ ]";
        }
        return floatToString(floats, 0, floats.length, "");
    }

    public static String floatToString(List<Float> floats, int start, int end) {
        if (floats == null) {
            return "[ ]";
        }
        return floatToString(floats, start, end, "");
    }

    public static String floatToString(float[] floats, int start, int end) {
        if (floats == null) {
            return "[ ]";
        }
        return floatToString(floats, start, end, "");
    }

    public static String floatToString(List<Float> floats, String unit) {
        if (floats == null) {
            return "[ ]";
        }
        return floatToString(floats, 0, floats.size(), unit);
    }

    public static String floatToString(float[] floats, String unit) {
        if (floats == null) {
            return "[ ]";
        }
        return floatToString(floats, 0, floats.length, unit);
    }


    /*
    core method
     */
    public static String intToString(List<Integer> list, int start, int end, String unit) {
        if (list == null) {
            return "[ ]";
        }
        if (list.size() == 0) {
            return "[ ]";
        }
        end = end <= list.size() ? end : list.size();
        if (start == end) {
            return "[" + list.get(start) + "]";
        }
        String temp = "[";
        for (int i = start; i < end; i++) {
            temp += list.get(i) + unit;
            if (i < end - 1) {
                temp += ",";
            }
        }
        return temp + "]";
    }

    /*
core method
 */
    public static String intToString(int[] ints, int start, int end, String unit) {
        if (start == end) {
            return "[" + ints[start] + "]";
        }
        if (ints.length == 0) {
            return "[ ]";
        }
        end = end <= ints.length ? end : ints.length;
        String temp = "[";
        for (int i = start; i < end; i++) {
            temp += ints[i] + unit;
            if (i < end - 1) {
                temp += ",";
            }
        }
        return temp + " ]";
    }

    /*
core method
 */
    public static String floatToString(float[] floats, int start, int end, String unit) {
        if (start == end) {
            return "[" + floats[start] + "]";
        }
        if (floats.length == 0) {
            return "[ ]";
        }
        end = end <= floats.length ? end : floats.length;
        String temp = "[";
        for (int i = start; i < end; i++) {
            temp += (Math.round(floats[i] * 10)) / 10.0 + unit;
            if (i < end - 1) {
                temp += ",";
            }
        }
        return temp + "]";
    }

    /*
    core method
     */
    public static String floatToString(List<Float> floats, int start, int end, String unit) {
        if (start == end) {
            return "[" + floats.get(start) + "]";
        }
        if (floats.size() == 0) {
            return "[ ]";
        }
        end = end <= floats.size() ? end : floats.size();
        String temp = "[";
        for (int i = start; i < end; i++) {
            temp += (Math.round(floats.get(i) * 10)) / 10.0 + unit;
            if (i < end - 1) {
                temp += ",";
            }
        }
        return temp + "]";
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


    public static int getIndexWithLogIndex(List<LogIndex> list, LogIndex logIndex) {
        int pos = -1;
        if (list == null) {
            return pos;
        }
        if (list.size() == 0) {
            return pos;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).logTime.equals(logIndex.logTime)) {
                return i;
            }
        }
        return pos;
    }

    public static float getFloatMax(List<Float> floats) {
        float temp = floats.get(0);
        for (int i = 0; i < floats.size(); i++) {
            if (floats.get(i) > temp) {
                temp = floats.get(i);
            }
        }
        return temp;
    }

    public static float getIntegerMax(List<Integer> list) {
        int temp = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > temp) {
                temp = list.get(i);
            }
        }
        return temp;
    }

    public static float getFloatMin(List<Float> floats) {
        float temp = floats.get(0);
        for (int i = 0; i < floats.size(); i++) {
            if (floats.get(i) < temp) {
                temp = floats.get(i);
            }
        }
        return temp;
    }

    public static float getIntegerMin(List<Integer> list) {
        int temp = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < temp) {
                temp = list.get(i);
            }
        }
        return temp;
    }


}





