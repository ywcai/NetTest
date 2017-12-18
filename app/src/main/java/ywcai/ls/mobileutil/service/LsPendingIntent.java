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
}
