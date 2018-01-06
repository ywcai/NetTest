package ywcai.ls.mobileutil.tools.Station.presenter;


import android.app.Activity;
import android.content.Context;

import rx.functions.Action1;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.util.statics.InStallService;
import ywcai.ls.mobileutil.global.util.statics.LsNotification;
import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.StationService;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.MainStationActionInf;


public class MainStationAction implements MainStationActionInf {
    StationService stationService = null;
    Activity activity;
    LsConnection lsConnection;
    Context context;


    public MainStationAction(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;
        lsConnection = new LsConnection(new Action1() {
            @Override
            public void call(Object o) {
                if (o != null) {
                    stationService = (StationService) o;
                } else {
                    stationService = null;
                }
            }
        });
        InStallService.bindService(this.context, StationService.class, lsConnection);
    }

    @Override
    public void startWork() {
        InStallService.waitService(stationService);
        if (stationService != null) {
            stationService.stationProcess.startProcess(context);
//            notification();
        }
    }

    private void notification() {
        LsNotification.notification(activity, "开启信号监听任务",
                AppConfig.TITLE_STATION, AppConfig.STATION_ACTIVITY_PATH, R.drawable.homepage_menu_station,
                AppConfig.INT_NOTIFICATION_PID_STATION);
    }

    @Override
    public void saveLogLocal() {
        if (stationService != null) {
            stationService.stationProcess.saveLogLocal();
        }
    }

    @Override
    public void saveLogRemote() {
        if (stationService != null) {
            stationService.stationProcess.saveLogRemote();
        }
    }

    @Override
    public void clearTask() {
        if (stationService != null) {
            stationService.stationProcess.clearTask();
        }
    }

    @Override
    public void selectFlexButton(int pos) {
        if (stationService != null) {
            stationService.stationProcess.setFlexButton(pos);
        }
    }

    @Override
    public void unRegPhoneStateListener() {
        if (stationService != null) {
            stationService.stationProcess.unRegPhoneListener();
        }
    }
}
