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
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.tools.Station.presenter.StationProcess;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationProcessInf;

/**
 * Created by zmy_11 on 2017/10/16.
 */

public class StationService extends Service {
    static final String TAG = "STATION";
    static final int PID = 7296;
    MyBinder binder = new MyBinder();
    public StationProcessInf stationProcessInf;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder.setService(StationService.this);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createInstall();
        notifyBuild();
    }

    private void createInstall() {
        stationProcessInf = new StationProcess();
        //判断版本，如果高于api22,则可检测双卡，否则检测单卡。

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            stationProcessInf.addDoubleSimCardListener();
        }
        else {
            stationProcessInf.addOnlySimCardListener();
        }
    }

    private void notifyBuild() {
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
