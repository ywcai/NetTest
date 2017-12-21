package ywcai.ls.mobileutil.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanPortResult;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanPortState;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanTaskExecute;

public class ScanPortService extends Service {
    MyBinder binder = new MyBinder();
    private ScanTaskExecute scanTaskExecute;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder.setService(ScanPortService.this);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //如果是退出了APP后新打开，则需要建任务并恢复任务
    public void recoveryExecuteTask(ScanPortState scanPortState, ScanPortResult scanPortResult) {
        if (scanTaskExecute == null) {
            scanTaskExecute = new ScanTaskExecute(scanPortState, scanPortResult);
        }
    }

    //当主动停止时，重新开始任务需要调用这个方法重置线程池
    public void resetThread(ScanPortState scanPortState, ScanPortResult scanPortResult) {
        if (scanTaskExecute != null) {
            scanTaskExecute.resetThread(scanPortState, scanPortResult);
        }
    }

    //直接在APP里面操作按钮时调用的
    public void startTask() {
        if (scanTaskExecute != null) {
            scanTaskExecute.startScanTask();
        }
    }

    //主动停止时候需要调用
    public void stopExecuteTask() {
        if (scanTaskExecute != null) {
            scanTaskExecute.breakThread();
        }
    }
}
