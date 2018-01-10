package ywcai.ls.mobileutil.tools.Station.presenter;


import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.HashMap;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.Station.model.SingleCardStationListener;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;
import ywcai.ls.mobileutil.tools.Station.model.StationState;
import ywcai.ls.mobileutil.tools.Station.model.inf.StationListenerFactoryInf;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationChangeListenerInf;


public class StationProcess implements StationChangeListenerInf {
    StationListenerFactoryInf stationListenerFactoryInf1, stationListenerFactoryInf2;
    private StationState stationState;
    private StationEntry currentEntry;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    NormalMode normalMode;
    DetailMode detailMode;
    private TelephonyManager telephonyManager;

    public StationProcess() {
        //从缓存初始化状态;
        currentEntry = new StationEntry();
        stationState = cacheProcess.getStationState();
        normalMode = new NormalMode(stationState, currentEntry);
        detailMode = new DetailMode();
        telephonyManager = (TelephonyManager) MainApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void recoveryUi() {
        setBaseNetInfo();
        sendMsgSwitchTopBtn();
        normalMode.recoveryChart();
    }

    public void regSingleListener() {
        //检测系统版本，确认是否支持双卡
        //若低于N版本，检测是否有双卡。
        //根据检测的结果选择注册不同的添加监听器;这里要实现监听的接口并处理接口返回的数据
        //场强和小区变化，需要分别注册实体监测，不然会吊死
        if (isOnlyListenerSingleCard()) {
            //这些需要在UI线程注册
            stationListenerFactoryInf1 = new SingleCardStationListener();
            stationListenerFactoryInf2 = new SingleCardStationListener();
            telephonyManager.listen((PhoneStateListener) stationListenerFactoryInf1, PhoneStateListener.LISTEN_CELL_LOCATION);
            telephonyManager.listen((PhoneStateListener) stationListenerFactoryInf2, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            stationListenerFactoryInf1.setChangeListener(this);
            stationListenerFactoryInf2.setChangeListener(this);
        } else {
            //如果是LEVEL21以上，处理双卡系统。
        }
    }

    //检测系统版本，选择哪种监听模式
    private boolean isOnlyListenerSingleCard() {
        return true;
    }


    //在外面去用.
    private void setBaseNetInfo() {
        currentEntry.netType = telephonyManager.getNetworkType();
        currentEntry.setNetTypeName();
        currentEntry.imei = telephonyManager.getDeviceId();
        currentEntry.cardNumber = telephonyManager.getSimSerialNumber();
        sendMsgTopTitle(currentEntry.netTypeCn);
    }


    @Override
    public void stationDataChange(HashMap<String, Integer> cells, HashMap<String, Integer> signals) {
        setBaseNetInfo();
        normalMode.refreshInfo(cells, signals);
        detailMode.refreshInfo(cells, signals);
    }


    public void saveLogLocal() {
        normalMode.saveLocal();
    }


    public void saveLogRemote() {
        normalMode.saveRemote();
    }

    public void clearTask() {
        normalMode.clearLog();
    }

    public void setFlexButton(int pos) {
        stationState.isShowFormat = pos == 0 ? true : false;
        cacheProcess.setStationState(stationState);
        sendMsgSwitchTopBtn();
    }

    private void sendMsgTopTitle(String netTypeName) {
        MsgHelper.sendEvent(GlobalEventT.station_set_toolbar_center_text, netTypeName, null);
    }

    private void sendMsgSwitchTopBtn() {
        MsgHelper.sendEvent(GlobalEventT.station_switch_top_btn, "", stationState.isShowFormat);
    }
}
