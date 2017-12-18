package ywcai.ls.mobileutil.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanPortProcess;

public class ScanPortService extends Service {
//    static final String TAG = AppConfig.TITLE_PORT;
//    static final int PID = AppConfig.INT_NOTIFICATION_PID_SCAN_PORT;
    MyBinder binder = new MyBinder();
    public ScanPortProcess scanPortProcess;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder.setService(ScanPortService.this);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InstallProcess();
    }

    private void InstallProcess() {
        if (scanPortProcess == null) {
            scanPortProcess = new ScanPortProcess();
        }
    }
}
