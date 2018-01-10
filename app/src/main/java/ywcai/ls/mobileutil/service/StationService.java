package ywcai.ls.mobileutil.service;


import android.app.Service;
import android.content.Intent;


import android.os.IBinder;
import android.support.annotation.Nullable;

import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.tools.Station.presenter.StationProcess;

public class StationService extends Service {
    MyBinder binder = new MyBinder();
    StationProcess stationProcess;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder.setService(StationService.this);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public StationProcess getStationProcess() {
        if (stationProcess == null) {
            stationProcess = new StationProcess();
            stationProcess.recoveryUi();
            stationProcess.regSingleListener();
        } else {
            stationProcess.recoveryUi();
        }
        return stationProcess;
    }
}
