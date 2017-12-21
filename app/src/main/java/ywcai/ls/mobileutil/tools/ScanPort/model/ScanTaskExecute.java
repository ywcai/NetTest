package ywcai.ls.mobileutil.tools.ScanPort.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import ywcai.ls.mobileutil.global.model.LsThreadFactory;
import ywcai.ls.mobileutil.global.util.statics.LsLog;


public class ScanTaskExecute {
    private ScanPortState scanPortState;
    private ScanPortResult scanPortResult;
    private ExecutorService executorService;

    public ScanTaskExecute(ScanPortState scanPortState, ScanPortResult scanPortResult) {
        this.scanPortState = scanPortState;
        this.scanPortResult = scanPortResult;
        if (executorService == null) {
            LsThreadFactory lsThreadFactory = new LsThreadFactory();
            executorService = Executors.newFixedThreadPool(30, lsThreadFactory);
        }
    }

    public void resetThread(ScanPortState scanPortState, ScanPortResult scanPortResult) {
        this.scanPortState = scanPortState;
        this.scanPortResult = scanPortResult;
        LsThreadFactory lsThreadFactory = new LsThreadFactory();
        executorService = Executors.newFixedThreadPool(30, lsThreadFactory);
    }

    public void startScanTask() {

        if (scanPortResult.maxCounts <= 1) {
            PortTest portTest = new PortTest();
            portTest.setConfig(scanPortState, scanPortResult);
            portTest.setTestPort(scanPortState.endPort);
            executorService.execute(portTest);
            return;
        }
        for (int i = scanPortState.startPort + scanPortResult.currentScanIndex; i <= scanPortState.endPort; i++) {
            PortTest portTest = new PortTest();
            portTest.setConfig(scanPortState, scanPortResult);
            portTest.setTestPort(i);
            executorService.execute(portTest);
        }
    }

    public void breakThread() {
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
    }


}
