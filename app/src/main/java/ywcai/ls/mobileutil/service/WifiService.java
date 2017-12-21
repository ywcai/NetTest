package ywcai.ls.mobileutil.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.tools.Wifi.presenter.ListenerWifiBroadcast;
import ywcai.ls.mobileutil.tools.Wifi.presenter.WifiHardControl;
import ywcai.ls.mobileutil.tools.Wifi.presenter.WifiProcess;


public class WifiService extends Service {
    MyBinder binder = new MyBinder();
    public WifiHardControl wifiHardControl;
    public ListenerWifiBroadcast listenerWifiBroadcast;

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

    public void startWifiScan(WifiProcess wifiProcess) {
        if (listenerWifiBroadcast == null) {
            listenerWifiBroadcast = new ListenerWifiBroadcast(wifiProcess);
        } else {
            listenerWifiBroadcast.setNewAction(wifiProcess);
        }
        if (wifiHardControl == null) {
            wifiHardControl = new WifiHardControl();
            wifiHardControl.startWifiScan();
        }
    }

    //控制WIFI系统的启停和计算系统是否自动刷新
    public void wifiResultSelfAdd() {
        if (wifiHardControl != null) {
            wifiHardControl.selfAdd++;
        }
    }

}
