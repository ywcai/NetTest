package ywcai.ls.mobileutil.tools.Station.model;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;


import java.lang.reflect.Field;

import java.util.HashMap;

import ywcai.ls.mobileutil.tools.Station.model.inf.StationListenerFactoryInf;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationChangeListenerInf;


public class SingleCardStationListener extends PhoneStateListener implements StationListenerFactoryInf {

    private StationChangeListenerInf stationChangeListenerInf;


    @Override
    public void setChangeListener(StationChangeListenerInf stationChangeListenerInf) {
        this.stationChangeListenerInf = stationChangeListenerInf;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        HashMap<String, Integer> signalFields = new HashMap<>();
        Field[] fields = signalStrength.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String fieldType = fields[i].getGenericType().toString();
            int value = -1;
            if (fieldType.contains("int")) {
                fields[i].setAccessible(true);
                try {
                    value = fields[i].getInt(signalStrength);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                signalFields.put(fieldName, value);
            }
        }
        //非常重要，否则第一次监听到onCellLocationChanged后，在4G状态下，不会触发该事件。
//        stationChangeListenerInf.resetCellListener();
        stationChangeListenerInf.stationDataChange(null, signalFields);
    }

    //双卡情况下系统会首先只会检测卡一，如果卡一没有，回检测卡2的变换
    @Override
    public void onCellLocationChanged(CellLocation location) {
        HashMap<String, Integer> locationFields = new HashMap<>();
        Field[] fields = location.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String fieldType = fields[i].getGenericType().toString();
            int value = -1;
            if (fieldType.contains("int")) {
                fields[i].setAccessible(true);
                try {
                    value = fields[i].getInt(location);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                locationFields.put(fieldName, value);
            }
        }
        stationChangeListenerInf.stationDataChange(locationFields, null);
    }


}
