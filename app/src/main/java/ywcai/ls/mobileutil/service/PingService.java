package ywcai.ls.mobileutil.service;


import android.app.Notification;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.GridLayout;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.model.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.main.view.MainActivity;
import ywcai.ls.mobileutil.tools.Ping.presenter.PingProcess;
import ywcai.ls.mobileutil.tools.Ping.presenter.inf.PingProcessInf;
import ywcai.ls.mobileutil.tools.Ping.view.PingActivity;


public class PingService extends Service {
    private static final String TAG = "PING_SERVICE";
    private static final int PID = 7733;
    private final IBinder binder = new MyBinder();
    public PingProcessInf pingProcessInf = null;

    NotificationCompat.Builder builder2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pingProcessInf = new PingProcess();
        initNotification();
    }

    private void initNotification() {
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.homepage_menu_ping)
                .setContentTitle("PING测试")
                .setContentText("任务运行")
                .setProgress(100, 0, false)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setTicker("PING测试任务开启");
        startForeground(PID, builder.build());
        builder2 = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.homepage_menu_ping)
                .setContentTitle("PING测试");
    }


    public void setProgress(int pos, int max) {
        builder2
                .setContentText("任务进度 " + pos + "/" + max + " " + pos * 100 / max + "%")
                .setProgress(max, pos, false)
                .setContentIntent(getPendingIntent("PING"));
        startForeground(PID, builder2.build());
    }
    public PendingIntent getPendingIntent(String extras) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);//非常重要，没加则在通知栏无法调其Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mainPendingIntent;
    }

    public void taskComplete(int flag, String tip) {
        NotificationManager notifyManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        switch (flag) {
            //任务完成
            case 0:
                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.homepage_menu_ping)
                        .setContentTitle("PING测试")
                        .setContentText("任务完成")
                        .setProgress(100, 100, false)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setTicker("PING测试任务完成")
                        .setContentIntent(getPendingIntent("PING"));
                startForeground(PID, builder.build());
                break;
            //暂停任务
            case 1:
                String context = builder2.mContentText.toString();
                builder2
                        .setContentText(builder2.mContentText.toString() + tip)
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setContentIntent(getPendingIntent("PING"));
                stopForeground(true);
                notifyManager.notify(PID, builder2.build());
                break;
            //手动终止任务
            case 2:
                stopForeground(true);
                notifyManager.cancel(PID);
                break;
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public PingService getPingService() {
            return PingService.this;
        }
    }
}


