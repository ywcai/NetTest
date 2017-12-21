package ywcai.ls.mobileutil.tools.Station.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.github.mikephil.charting.charts.LineChart;

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
        recoveryTopBtn();
        normalMode.recoveryChart();
    }

    private void recoveryTopBtn() {
        sendMsgSwitchTopBtn();
    }


    //开始监听需要的基站数据,如果是新建，才启动，否则仅调用 recoveryAllData();
    public void startProcess(Context context) {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //检测系统版本，确认是否支持双卡
        //若低于N版本，检测是否有双卡。
        //根据检测的结果选择注册不同的添加监听器;这里要实现监听的接口并处理接口返回的数据
        //场强和小区变化，需要分别注册实体监测，不然会吊死
        if (!checkNetType()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sendMsgSnackBarTip("你拒绝了系统的权限申请！", false);
            } else {
                sendMsgSnackBarTip("你拒绝了系统的权限申请！", true);
            }
            return;
        }
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

    public void unRegPhoneListener() {
        telephonyManager.listen((PhoneStateListener) stationListenerFactoryInf1, PhoneStateListener.LISTEN_NONE);
        telephonyManager.listen((PhoneStateListener) stationListenerFactoryInf2, PhoneStateListener.LISTEN_NONE);
    }

    //检测系统版本，选择哪种监听模式
    private boolean isOnlyListenerSingleCard() {
        return true;
    }


    //在外面去用.
    private boolean checkNetType() {
        currentEntry.netType = telephonyManager.getNetworkType();
        currentEntry.setNetTypeName();
        if (PackageManager.PERMISSION_GRANTED !=
                MainApplication.getInstance().getApplicationContext().
                        getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", "ywcai.ls.mobileutil")) {
            return false;
        }
        if (PackageManager.PERMISSION_GRANTED !=
                MainApplication.getInstance().getApplicationContext().
                        getPackageManager().checkPermission("android.permission.ACCESS_COARSE_LOCATION", "ywcai.ls.mobileutil")) {
            return false;
        }
        currentEntry.imei = telephonyManager.getDeviceId();
        currentEntry.cardNumber = telephonyManager.getSimSerialNumber();
        sendMsgTopTitle(currentEntry.netTypeCn);
        return true;
    }


    @Override
    public void stationDataChange(HashMap<String, Integer> cells, HashMap<String, Integer> signals) {
        checkNetType();
        normalMode.refreshInfo(cells, signals);
        detailMode.refreshInfo(cells, signals);
    }


    public void addTask() {
    }

    public void removeTask() {
    }

    public void popOperatorMenu(boolean isShow) {
        sendMsgPopMenu(isShow);
    }


    public void saveLogLocal() {
        normalMode.saveLocal();
        sendMsgPopMenu(false);
    }

    public void saveLogRemote() {
        normalMode.saveRemote();
        sendMsgPopMenu(false);
    }

    public void clearTask() {
        normalMode.clearLog();
        sendMsgPopMenu(false);
    }

    public void saveBitmap(LineChart lineChart) {

    }

    public void setFlexButton(int pos) {
        stationState.isShowFormat = pos == 0 ? true : false;
        cacheProcess.setStationState(stationState);
        sendMsgSwitchTopBtn();
    }


    private void sendMsgTopTitle(String netTypeName) {
        MsgHelper.sendEvent(GlobalEventT.station_set_toolbar_center_text, netTypeName, null);
    }

    private void sendMsgPopMenu(boolean isShow) {
        MsgHelper.sendEvent(GlobalEventT.station_pop_dialog, "", isShow);
    }

    private void sendMsgSwitchTopBtn() {
        MsgHelper.sendEvent(GlobalEventT.station_switch_top_btn, "", stationState.isShowFormat);
    }

    private void sendMsgSnackBarTip(String tip, boolean isSuccess) {
        MsgHelper.sendEvent(GlobalEventT.station_bottom_snack_tip, tip, isSuccess);
    }

}
