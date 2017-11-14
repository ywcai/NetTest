package ywcai.ls.mobileutil.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import ywcai.ls.mobileutil.tools.Wifi.presenter.ListenerWifiBroadcast;
import ywcai.ls.mobileutil.tools.Wifi.presenter.WifiHardControl;
import ywcai.ls.mobileutil.tools.Wifi.presenter.WifiProcess;

/**
 * Created by zmy_11 on 2017/10/16.
 */

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

//    public void notifyAddTask(int count) {
//        NotificationCompat.Builder tipBuilder = new NotificationCompat.Builder(this);
//        LsPendingIntent lsPendingIntent = new LsPendingIntent();
//        tipBuilder
//                .setContentIntent(lsPendingIntent.getPendingForStartActivity(this, AppConfig.WIFI_ACTIVITY_PATH))
//                .setAutoCancel(true)
//                .setOngoing(false)
//                .setSmallIcon(R.drawable.homepage_menu_wifi)
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.nav))
//                .setContentTitle(TAG + " 添加了一个监听任务")
//                .setContentText(TAG + " 后台有" + count + "个任务正在运行");
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(PID, tipBuilder.build());
//    }

}
