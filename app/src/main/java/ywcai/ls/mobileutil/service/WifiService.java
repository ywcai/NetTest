package ywcai.ls.mobileutil.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import ywcai.ls.mobileutil.tools.Wifi.presenter.ListenerWifiBroadcast;
import ywcai.ls.mobileutil.tools.Wifi.presenter.WifiHardControl;
import ywcai.ls.mobileutil.tools.Wifi.presenter.WifiProcess;


public class WifiService extends Service {
    MyBinder binder = new MyBinder();
    public WifiProcess wifiDataProcess;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder.setService(WifiService.this);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void installProcess() {
        if (wifiDataProcess != null) {
            //唤醒缓存在内存的数据恢复UI
            wifiDataProcess.recoveryAllData();
            return;
        }
        WifiHardControl wifiControl = new WifiHardControl();//控制WIFI系统的启停和计算系统是否自动刷新
        wifiDataProcess = new WifiProcess(wifiControl);//处理系统广播的WIFI数据
        //注册广播，注入action
        new ListenerWifiBroadcast(wifiDataProcess);//注册系统广播
        wifiControl.startWifiScan();
    }
}
