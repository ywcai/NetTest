package ywcai.ls.mobileutil.tools.ScanLan.presenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.LsThreadFactory;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.ScanLan.model.LocalNetInfo;
import ywcai.ls.mobileutil.tools.ScanLan.model.PingRunnable;

import ywcai.ls.mobileutil.tools.ScanLan.model.ScanLanState;

/**
 * Created by zmy_11 on 2017/12/14.
 */

public class ScanLanAction {
    private ExecutorService executorService;
    private ScanLanState scanLanState;


    public ScanLanAction() {
        if (executorService == null) {
            LsThreadFactory threadFactory = new LsThreadFactory();
            executorService = Executors.newFixedThreadPool(30, threadFactory);
        }
    }

    public void startWork() {
        //局部变量在这里可以刷新一次本地IP地址。
        String maskNet = getLocalMaskNet();
        scanLanState = new ScanLanState();
        scanLanState.maskNet = maskNet;
        scanLanState.scanCount = 0;
        scanLanState.maxCount=254;
        for (int i = 1; i <=  scanLanState.maxCount; i++) {
            PingRunnable pingRunnable = new PingRunnable();
            pingRunnable.setScanState(scanLanState);
            pingRunnable.setIndex(i);
            executorService.execute(pingRunnable);
        }
    }

    private String getLocalMaskNet() {
        LocalNetInfo localNetInfo = new LocalNetInfo();
        String localIp = localNetInfo.getLocalIp();
        MsgHelper.sendEvent(GlobalEventT.scan_lan_refresh_local_ip, localIp, null);
        String[] args = localIp.split("\\.");
        return args[0] + "." + args[1] + "." + args[2] + ".";
    }


    public void stopScanThread() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
