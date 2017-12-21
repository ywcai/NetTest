package ywcai.ls.mobileutil.global.util.statics;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.service.LsPendingIntent;

import static android.content.Context.NOTIFICATION_SERVICE;


public class LsNotification {

    public static void notification(Activity activity, String tip, String title, String activityPath, int icon, int pid) {
        NotificationCompat.Builder tipBuilder = new NotificationCompat.Builder(activity);
        LsPendingIntent lsPendingIntent = new LsPendingIntent();
        tipBuilder
                .setContentIntent(lsPendingIntent.getPendingForStartActivity(activity, activityPath))
                .setAutoCancel(true)
                .setOngoing(false)
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.nav))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(title)
                .setContentText(tip);
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(pid, tipBuilder.build());
    }
}
