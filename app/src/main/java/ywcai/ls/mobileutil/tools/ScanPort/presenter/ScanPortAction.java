package ywcai.ls.mobileutil.tools.ScanPort.presenter;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.global.util.statics.InStallService;
import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.ScanPortService;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanPortProcess;
import ywcai.ls.mobileutil.tools.ScanPort.presenter.inf.ScanPortActionInf;


public class ScanPortAction implements ScanPortActionInf {
    ScanPortService scanPortService = null;
    LsConnection lsConnection;
    ScanPortProcess scanPortProcess;

    public ScanPortAction() {
        scanPortProcess = new ScanPortProcess();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bindService();
                waitService();
            }
        }).start();
    }

    //自动绑定后台服务
    private void bindService() {
        lsConnection = new LsConnection(new Action1() {
            @Override
            public void call(Object o) {
                if (o != null) {
                    scanPortService = (ScanPortService) o;
                } else {
                    scanPortService = null;
                }
            }
        });
        InStallService.bindService(ScanPortService.class, lsConnection);
    }

    @Override
    public void clickOperatorBtn() {
        scanPortProcess.selectProcess();
    }

    @Override
    public void addScanTask(String ip, String startText, String endText) {
        scanPortProcess.addScanPortTask(ip, startText, endText);
    }

    @Override
    public void recoveryUI() {
        scanPortProcess.recoveryUiState();
    }

    @Override
    public void waitService() {
        InStallService.waitService(scanPortService);//绑定服务是异步的，必须等待，否则可能启动时空指针
        scanPortProcess.setScanPortService(scanPortService);
    }
}
