package ywcai.ls.mobileutil.service;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.welcome.view.WelComeActivity;

public class LsPendingIntent {
    public PendingIntent getPendingForStartActivity(Context context, String forwardUrl) {
        Intent intent = new Intent();
        intent.setClass(context, WelComeActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
//    public PendingIntent getBroadcastForCancalNotify(Context context) {
//        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
//        intent.setAction(NotificationBroadcastReceiver.BROADCAST_ACTION_FLAG);
//        intent.putExtra(NotificationBroadcastReceiver.BROADCAST_ACTION_FLAG, NotificationBroadcastReceiver.BROADCAST_TYPE_CANCAL);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return pendingIntent;
//    }
//
//    public PendingIntent getBroadcastForStartApp(Context context, String forwardPath) {
//        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
//        intent.setAction(NotificationBroadcastReceiver.BROADCAST_ACTION_FLAG);
//        intent.putExtra(NotificationBroadcastReceiver.BROADCAST_ACTION_FLAG, NotificationBroadcastReceiver.BROADCAST_TYPE_CREATE_ACTIVITY);
//        intent.putExtra(AppConfig.ROUTER_FLAG, forwardPath);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return pendingIntent;
//    }
}
