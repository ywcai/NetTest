package ywcai.ls.mobileutil.tools.ScanPort.presenter;

import android.content.Context;

import rx.functions.Action1;
import ywcai.ls.mobileutil.global.util.statics.InStallService;
import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.ScanPortService;
import ywcai.ls.mobileutil.tools.ScanPort.presenter.inf.ScanPortActionInf;

public class ScanPortAction implements ScanPortActionInf {
    ScanPortService scanPortService = null;
    LsConnection lsConnection;
    Context context;

    public ScanPortAction(Context context) {
        this.context = context;
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
        InStallService.bindService(this.context, ScanPortService.class, lsConnection);
    }


    @Override
    public void clickOperatorBtn() {
        if (scanPortService != null) {
            scanPortService.scanPortProcess.selectProcess();
        }
    }

    @Override
    public void addScanTask(String ip, String startText, String endText) {
        if (scanPortService != null) {
            scanPortService.scanPortProcess.addScanPortTask(ip, startText, endText);
        }
    }

    @Override
    public void recoveryUI() {
        if (scanPortService != null) {
            scanPortService.scanPortProcess.recoveryUiState();
        }
    }

    @Override
    public void waitService() {
        InStallService.waitService(scanPortService);//绑定服务是异步的，必须等待，否则可能启动时空指针
    }
}
