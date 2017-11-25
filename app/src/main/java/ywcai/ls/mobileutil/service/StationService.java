package ywcai.ls.mobileutil.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.tools.Station.presenter.StationProcess;

/**
 * Created by zmy_11 on 2017/10/16.
 */

public class StationService extends Service {
    static final String TAG = AppConfig.TITLE_STATION;
    static final int PID = AppConfig.INT_NOTIFICATION_PID_STATION;
    MyBinder binder = new MyBinder();
    public StationProcess stationProcess;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder.setService(StationService.this);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InstallProcess();
    }

    private void InstallProcess() {
        if (stationProcess != null) {
            //直接冲内存恢复数据渲染UI
            stationProcess.recoveryAllData();
            return;
        }
        stationProcess = new StationProcess();//构造函数中去初始化数据。??
    }

    public void setForegroundTask() {
        NotificationCompat.Builder tipBuilder = new NotificationCompat.Builder(this);
        LsPendingIntent lsPendingIntent = new LsPendingIntent();
        tipBuilder
                .setContentIntent(lsPendingIntent.getPendingForStartActivity(this, AppConfig.STATION_ACTIVITY_PATH))
                .setAutoCancel(true)
                .setOngoing(false)
                .setSmallIcon(R.drawable.homepage_menu_station)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.nav))
                .setContentTitle(TAG + "任务正在运行")
                .setContentText(TAG + "任务正在运行");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(PID, tipBuilder.build());
    }
}
