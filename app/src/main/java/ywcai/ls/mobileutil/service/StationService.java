package ywcai.ls.mobileutil.service;


import android.app.Service;
import android.content.Intent;


import android.os.IBinder;
import android.support.annotation.Nullable;

import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.tools.Station.presenter.StationProcess;

public class StationService extends Service {
    MyBinder binder = new MyBinder();
    public StationProcess stationProcess;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder.setService(StationService.this);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InstallProcess();
    }

    private void InstallProcess() {
        if (stationProcess != null) {
            //不处理可以直接绑定上一次运行的内存对象恢复渲染UI
            LsLog.saveLog("old ");
            return;
        }
        LsLog.saveLog("new ");
        stationProcess = new StationProcess();//构造函数中去初始化数据。
    }
}
