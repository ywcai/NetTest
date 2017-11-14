package ywcai.ls.mobileutil.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.util.statics.LsLog;


public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static final String BROADCAST_ACTION_FLAG = "YWCAI_LS_NET_TEST";
    public static final int BROADCAST_TYPE_CANCAL = 0;
    public static final int BROADCAST_TYPE_CREATE_ACTIVITY = 1;
    private Service service;

    public NotificationBroadcastReceiver(Service service) {
        this.service = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LsLog.saveLog("收到点击广播了");
        int type = intent.getIntExtra(BROADCAST_ACTION_FLAG, -1);
        switch (type) {
            case BROADCAST_TYPE_CANCAL:
                cancalNotify();
                break;
            case BROADCAST_TYPE_CREATE_ACTIVITY:
                startApp(context, intent);
                break;
        }
    }

    private void startApp(Context context, Intent intent) {
        LsLog.saveLog("收到点击广播了:startApp");
        String routerArgs = intent.getStringExtra(AppConfig.ROUTER_FLAG);
        ARouter.getInstance().build(AppConfig.MAIN_ACTIVITY_PATH).withString(AppConfig.ROUTER_FLAG,routerArgs).navigation(context);
        service.stopForeground(true);
    }

    private void cancalNotify() {
        LsLog.saveLog("收到点击广播了:stopApp");
        service.stopForeground(true);
    }
}
