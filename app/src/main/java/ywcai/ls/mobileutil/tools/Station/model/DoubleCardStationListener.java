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


public class DoubleCardStationListener implements StationListenerFactoryInf {
    StationEntry stationEntry = new StationEntry();
    private StationChangeListenerInf stationChangeListenerInf;
    private Context context = MainApplication.getInstance().getApplicationContext();
    private TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);


    public DoubleCardStationListener() {

    }
//
//    @Override
//    public StationBaseInfo getBaseInfo() {
//        //每次初始化或恢复服务时都重新获取；
//        return new StationBaseInfo();
//    }

    @Override
    public void setChangeListener(StationChangeListenerInf stationChangeListenerInf) {
        this.stationChangeListenerInf = stationChangeListenerInf;
    }
}
