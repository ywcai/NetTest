package ywcai.ls.mobileutil.tools.Wifi.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;

/**
 * Created by zmy_11 on 2017/10/15.
 */

public class ListenerWifiBroadcast extends BroadcastReceiver {
    public Action1 action1;
    public Context context= MainApplication.getInstance().getApplicationContext();

    public ListenerWifiBroadcast(Action1 action1) {
        this.action1 = action1;
        registerBoardCast();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Observable.just(intent).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(action1);
    }
    private void registerBoardCast()
    {
        context.registerReceiver(this, new IntentFilter(
                WifiManager.WIFI_STATE_CHANGED_ACTION));
        context.registerReceiver(this, new IntentFilter(
                WifiManager.RSSI_CHANGED_ACTION));
        context.registerReceiver(this, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        context.registerReceiver(this, new IntentFilter(
                WifiManager.EXTRA_WIFI_STATE));
        context.registerReceiver(this, new IntentFilter(
                WifiManager.NETWORK_STATE_CHANGED_ACTION));
    }
}
