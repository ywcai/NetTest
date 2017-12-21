package ywcai.ls.mobileutil.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.service.inf.ServiceControlInf;
import ywcai.ls.mobileutil.tools.Ping.presenter.PingProcess;
import ywcai.ls.mobileutil.tools.Ping.presenter.inf.PingProcessInf;


public class PingService extends Service implements ServiceControlInf {
    final String TAG = AppConfig.TITLE_PING;
    final int PID = AppConfig.INT_NOTIFICATION_PID_PING;
    MyBinder binder = new MyBinder();
    public PingProcessInf pingProcessInf = null;
    NotificationCompat.Builder progressBuilder;
    LsPendingIntent lsPendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder.setService(PingService.this);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pingProcessInf = new PingProcess();
        lsPendingIntent = new LsPendingIntent();
        progressBuilder = new NotificationCompat.Builder(this);
        progressBuilder
                .setContentIntent(lsPendingIntent.getPendingForStartActivity(this, AppConfig.PING_ACTIVITY_PATH))
                .setAutoCancel(true)
                .setOngoing(false);
    }


    @Override
    public void setTaskProgress(int pos, int max) {
        progressBuilder
                .setSmallIcon(R.drawable.homepage_menu_ping)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.nav))
                .setContentTitle(TAG + "任务正在执行")
                .setContentText("任务进度 " + pos + ":" + max + " " + pos * 100 / max + "%")
                .setProgress(max, pos, false)
                .setAutoCancel(true)
                .setOngoing(false);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(PID, progressBuilder.build());
    }

    @Override
    public void onDestroy() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        super.onDestroy();
    }

    @Override
    public void setTaskCompleteTip(int completeType) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        switch (completeType) {
            //任务完成
            case 0:
                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.homepage_menu_ping)
                        .setContentTitle(TAG + "测试完成")
                        .setContentText("开启新任务前，数据会临时存储！")
                        .setTicker(TAG + "测试完成")
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.nav))
                        .setContentIntent(lsPendingIntent.getPendingForStartActivity(this, AppConfig.PING_ACTIVITY_PATH))
                        .setAutoCancel(true)
                        .setOngoing(false);
                notificationManager.notify(PID, builder.build());
                break;
            //暂停任务
            case 1:
                progressBuilder
                        .setContentText(progressBuilder.mContentText.toString() + " 手动暂停任务");
                notificationManager.notify(PID, progressBuilder.build());
                break;
            //手动终止任务
            case 2:
                notificationManager.cancelAll();
                break;
        }
    }
}