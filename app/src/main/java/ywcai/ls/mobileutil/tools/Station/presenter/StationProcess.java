package ywcai.ls.mobileutil.tools.Station.presenter;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.github.mikephil.charting.charts.LineChart;

import java.util.HashMap;


import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.Station.model.DoubleCardStationListener;
import ywcai.ls.mobileutil.tools.Station.model.SingleCardStationListener;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;
import ywcai.ls.mobileutil.tools.Station.model.StationState;
import ywcai.ls.mobileutil.tools.Station.model.inf.StationListenerFactoryInf;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationChangeListenerInf;


public class StationProcess implements StationChangeListenerInf {
    private Context context = MainApplication.getInstance().getApplicationContext();
    private StationState stationState;
    private StationEntry stationEntry;
    //场强和小区变化，需要分别注册实体监测，不然会吊死
    StationListenerFactoryInf stationListenerFactoryInf1, stationListenerFactoryInf2;
    NormalMode normalMode;
    DetailMode detailMode;
    DtTask privateTask;
    LogTask logTask;
    private TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

    public StationProcess() {
        //从缓存初始化状态;
        stationState = CacheProcess.getInstance().getStationState();
        stationEntry = new StationEntry();
        checkNetType();
        normalMode = new NormalMode(stationEntry);
        detailMode = new DetailMode();
        recoveryAllData();//从station恢复状态
    }

    //如果service还在后台，则仅恢复UI状态.
    public void recoveryAllData() {
//        stationBaseInfo = stationListenerFactoryInf.getBaseInfo();
    }

    //开始监听需要的基站数据,如果是新建，才启动，否则仅调用 recoveryAllData();
    public void startProcess() {
        //检测系统版本，确认是否支持双卡
        //若低于N版本，检测是否有双卡。
        //根据检测的结果选择注册不同的添加监听器;这里要实现监听的接口并处理接口返回的数据
        if (isOnlyListenerSingleCard()) {
            //这些需要在UI线程注册
            stationListenerFactoryInf1 = new SingleCardStationListener();
            stationListenerFactoryInf2 = new SingleCardStationListener();
            telephonyManager.listen((PhoneStateListener) stationListenerFactoryInf1, PhoneStateListener.LISTEN_CELL_LOCATION);
            telephonyManager.listen((PhoneStateListener) stationListenerFactoryInf2, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        } else {
            //如果是LEVEL21以上，处理双卡系统。
        }
        stationListenerFactoryInf1.setChangeListener(this);
        stationListenerFactoryInf2.setChangeListener(this);
    }

    //检测系统版本，选择哪种监听模式
    private boolean isOnlyListenerSingleCard() {
        return true;
    }


    //在外面去用.
    private void checkNetType() {
        stationEntry.netType = telephonyManager.getNetworkType();
        stationEntry.setNetTypeName();
        stationEntry.imei = telephonyManager.getDeviceId();
        stationEntry.cardNumber = telephonyManager.getSimSerialNumber();
        //在TOP顶部显示网络制式
        sendMsgTopTitle(stationEntry.netTypeCn);
    }


    @Override
    public void stationDataChange(HashMap<String, Integer> cells, HashMap<String, Integer> signals) {
        checkNetType();
        normalMode.refreshInfo(cells, signals);
        detailMode.refreshInfo(cells, signals);
    }

    @Override
    public void resetCellListener() {
//        telephonyManager.listen((PhoneStateListener) stationListenerFactoryInf, PhoneStateListener.LISTEN_CELL_LOCATION);
    }


    public void addTask() {
    }

    public void removeTask() {
    }

    public void popOperatorMenu(boolean isShow) {
    }

    public void saveLogLocal() {
    }

    public void saveLogRemote() {
    }

    public void clearTask() {
    }

    public void saveBitmap(LineChart lineChart) {
    }


//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
//    private  void getSimCard()
//    {
//        String[] strSimCard=new String[2];
//        SubscriptionManager mSubscriptionManager = SubscriptionManager.from(context);
//        String tip="";
//        tip=mSubscriptionManager.getActiveSubscriptionInfoCount()+"|"+mSubscriptionManager.getActiveSubscriptionInfoCountMax()+
//                "|"+mSubscriptionManager.getActiveSubscriptionInfoList().size();
//        MsgHelper.sendEvent(GlobalEventT.station_set_entry_change,tip, null);
//    }
//    private String getSimCard1()
//    {
//        return "";
//    }
//    private String getSimCard2()
//    {
//        return "";
//    }
//
//
//
//    public void addDoubleSimCardListener() {
//
//    }
//
//
//    public void addOnlySimCardListener() {
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        SingleCardStationListener stationListener=new SingleCardStationListener();
//        try {
//            telephonyManager.listen(stationListener, PhoneStateListener.LISTEN_CELL_LOCATION);
//            telephonyManager.listen(stationListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
//        }
//        catch (Exception e)
//        {
//        }
//    }

    private void sendMsgTopTitle(String netTypeName) {
        MsgHelper.sendEvent(GlobalEventT.station_set_toolbar_center_text, netTypeName, null);
    }
}
