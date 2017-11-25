package ywcai.ls.mobileutil.tools.Station.model;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;

import ywcai.ls.mobileutil.tools.Station.model.inf.StationListenerFactoryInf;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationChangeListenerInf;


public class SingleCardStationListener extends PhoneStateListener implements StationListenerFactoryInf {
    StationEntry stationEntry = new StationEntry();
    private StationChangeListenerInf stationChangeListenerInf;
    private Context context = MainApplication.getInstance().getApplicationContext();
    private TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

    @Override
    public StationBaseInfo getBaseInfo() {
        //每次初始化或恢复服务时都重新获取；
        return new StationBaseInfo();
    }

    @Override
    public void setChangeListener(StationChangeListenerInf stationChangeListenerInf) {
        this.stationChangeListenerInf = stationChangeListenerInf;
    }


    //    Field Lte_rsp = null;
    //        Field Wcdma_rsp = null;
//        try {
//            stationEntry.netType = telephonyManager.getNetworkType();
//            Lte_rsp = clazz.getDeclaredField("mLteRsrp");
//            Lte_rsp.setAccessible(true);
//            Wcdma_rsp = clazz.getDeclaredField("mWcdmaRscp");
//            Wcdma_rsp.setAccessible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (stationEntry.netType == TelephonyManager.NETWORK_TYPE_EDGE || stationEntry.netType == TelephonyManager.NETWORK_TYPE_GPRS) {
//            stationEntry.rsp = signalStrength.getGsmSignalStrength();//2G
//        } else if (stationEntry.netType == TelephonyManager.NETWORK_TYPE_LTE) {
//
//            try {
//                stationEntry.rsp = Lte_rsp.getInt(signalStrength);
//            } catch (Exception e) {
//
//            }
//        } else {
//            try {
//                stationEntry.rsp = Wcdma_rsp.getInt(signalStrength);
//            } catch (Exception e) {
//
//            }
//        }
    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {

        Field[] field = signalStrength.getClass().getDeclaredFields();
        String[] modelName = new String[field.length];
        String[] modeType = new String[field.length];
        String[] modelValue = new String[field.length];
        String tip = "";
        for (int i = 0; i < field.length; i++) {
            // 获取属性的名字
            modelName[i] = field[i].getName();
            modeType[i] = field[i].getGenericType().toString();
            field[i].setAccessible(true);
            if (modeType[i].endsWith("String")) {
                modelValue[i] = field[i].toString();
            } else if (modeType[i].endsWith("int") || modeType[i].endsWith("Integer")) {
                try {
                    modelValue[i] = field[i].getInt(signalStrength) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (modeType[i].endsWith("boolean")) {
                try {
                    modelValue[i] = field[i].getBoolean(signalStrength) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                modelValue[i] = "none";
            }
            if (!modelName[i].contains("_")) {
                tip += "[" + modelName[i] + ":" + modeType[i] + ":" + modelValue[i] + "] ";
            }
        }

//        tip += getMethod(signalStrength);
//        sendStationChange(tip);
//        getCellLocation();

        //反射处理信号强度
        stationChangeListenerInf.stationDataChange(stationEntry);
    }

    @Override
    public void onCellLocationChanged(CellLocation location) {
//        stationEntry.setBaseData();
        Field[] field = location.getClass().getDeclaredFields();
        String[] modelName = new String[field.length];
        String[] modeType = new String[field.length];
        String[] modelValue = new String[field.length];
        String tip = "";
        for (int i = 0; i < field.length; i++) {
            // 获取属性的名字
            modelName[i] = field[i].getName();
            modeType[i] = field[i].getGenericType().toString();
            field[i].setAccessible(true);
            if (modeType[i].endsWith("String")) {
                modelValue[i] = field[i].toString();
            } else if (modeType[i].endsWith("int") || modeType[i].endsWith("Integer")) {
                try {
                    modelValue[i] = field[i].getInt(location) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (modeType[i].endsWith("boolean")) {
                try {
                    modelValue[i] = field[i].getBoolean(location) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                modelValue[i] = "none";
            }
            if (!modelName[i].contains("_")) {
                tip += "[" + modelName[i] + ":" + modeType[i] + ":" + modelValue[i] + "] ";
            }
        }
        stationChangeListenerInf.stationDataChange(stationEntry);
//        stationEntry.lac = ((GsmCellLocation) location).getLac();
//        stationEntry.cid = ((GsmCellLocation) location).getCid();

//        sendCellChange(tip);

//        sendStationChange();
    }


//    public void sendStationChange(String tip) {
//        MsgHelper.sendEvent(GlobalEventT.station_set_entry_change, tip, null);
//    }
//
//    public void sendCellChange(String tip) {
//        MsgHelper.sendEvent(GlobalEventT.station_set_cell_change, tip, null);
//    }
//
//    public void sendCellChange1(String tip) {
//        MsgHelper.sendEvent(GlobalEventT.station_set_cell_change1, tip, null);
//    }

//    public void getCellLocation() {
//        CellLocation cell = telephonyManager.getCellLocation();
//        if (cell == null) {
//            return;
//        }
//        Field[] field = cell.getClass().getDeclaredFields();
//        String[] modelName = new String[field.length];
//        String[] modeType = new String[field.length];
//        String[] modelValue = new String[field.length];
//        String tip = "";
//        for (int i = 0; i < field.length; i++) {
//            // 获取属性的名字
//            modelName[i] = field[i].getName();
//            modeType[i] = field[i].getGenericType().toString();
//            field[i].setAccessible(true);
//            if (modeType[i].endsWith("String")) {
//                modelValue[i] = field[i].toString();
//            } else if (modeType[i].endsWith("int") || modeType[i].endsWith("Integer")) {
//                try {
//                    modelValue[i] = field[i].getInt(cell) + "";
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            } else if (modeType[i].endsWith("boolean")) {
//                try {
//                    modelValue[i] = field[i].getBoolean(cell) + "";
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                modelValue[i] = "none";
//            }
//            if (!modelName[i].contains("_")) {
//                tip += "[" + modelName[i] + ":" + modeType[i] + ":" + modelValue[i] + "] ";
//            }
//        }
//        sendCellChange1(tip);
//    }

//    public String getMethod(SignalStrength signalStrength) {
//
//        Method[] methods = signalStrength.getClass().getDeclaredMethods();
//        String[] modelName = new String[methods.length];
//        String[] modeType = new String[methods.length];
//        String[] modelValue = new String[methods.length];
//        String tip = "";
//        for (int i = 0; i < methods.length; i++) {
//            modelName[i] = methods[i].getName();
//            modeType[i] = methods[i].getGenericReturnType().toString();
//            methods[i].setAccessible(true);
//            tip += "[" + modelName[i] + ":" + modeType[i] + "]";
//        }
//        return tip;
//    }


}
