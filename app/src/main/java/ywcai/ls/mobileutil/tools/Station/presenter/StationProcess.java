package ywcai.ls.mobileutil.tools.Station.presenter;

import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;

import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.tools.Station.model.DoubleCardStationListener;
import ywcai.ls.mobileutil.tools.Station.model.SingleCardStationListener;
import ywcai.ls.mobileutil.tools.Station.model.StationBaseInfo;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;
import ywcai.ls.mobileutil.tools.Station.model.StationState;
import ywcai.ls.mobileutil.tools.Station.model.inf.StationListenerFactoryInf;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationChangeListenerInf;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;

/**
 * Created by zmy_11 on 2017/10/30.
 */

public class StationProcess implements StationChangeListenerInf {
    private Context context = MainApplication.getInstance().getApplicationContext();
    private StationState stationState;
    private StationEntry stationEntry;
    StationBaseInfo stationBaseInfo;
    StationListenerFactoryInf stationListenerFactoryInf;

    public StationProcess() {
        //从缓存初始化状态;
        stationState = CacheProcess.getInstance().getStationState();
        recoveryAllData();//从station恢复状态
    }

    //如果service还在后台，则仅恢复UI状态.
    public void recoveryAllData() {
        stationBaseInfo = stationListenerFactoryInf.getBaseInfo();
    }
    //开始监听需要的基站数据,如果是新建，才启动，否则仅调用 recoveryAllData();
    public void startProcess() {
        //检测系统版本，确认是否支持双卡
        //若低于N版本，检测是否有双卡。
        //根据检测的结果选择注册不同的添加监听器;这里要实现监听的接口并处理接口返回的数据

        if (isOnlyListenerSingleCard()) {
            stationListenerFactoryInf = new SingleCardStationListener();

        } else {
            stationListenerFactoryInf = new DoubleCardStationListener();
        }
        stationListenerFactoryInf.setChangeListener(this);
    }

    //检测系统版本，选择哪种监听模式
    private boolean isOnlyListenerSingleCard() {
        return false;
    }


    @Override
    public void stationDataChange(StationEntry stationEntry) {
        this.stationEntry = stationEntry;
        CoreStationProcess(stationEntry);
    }

    private void CoreStationProcess(StationEntry stationEntry) {
        //需要怎么处理，在这里说了算.
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


}
