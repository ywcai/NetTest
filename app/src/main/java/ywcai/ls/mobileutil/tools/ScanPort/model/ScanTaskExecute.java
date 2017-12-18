package ywcai.ls.mobileutil.tools.ScanPort.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import ywcai.ls.mobileutil.global.model.LsThreadFactory;


public class ScanTaskExecute {
    private ScanPortState scanPortState;
    private ScanPortResult scanPortResult;
    private ExecutorService executorService;
    private LsThreadFactory lsThreadFactory;

    public ScanTaskExecute(ScanPortState scanPortState, ScanPortResult scanPortResult) {
        this.scanPortState = scanPortState;
        this.scanPortResult = scanPortResult;
        if (executorService == null) {
            lsThreadFactory = new LsThreadFactory();
            executorService = Executors.newFixedThreadPool(30, lsThreadFactory);
        }
    }

    public void startScanTask() {

        if (scanPortResult.maxCounts <= 1) {
            PortTest portTest = new PortTest();
            portTest.setConfig(scanPortState, scanPortResult);
            portTest.setTestPort(scanPortState.endPort);
            executorService.execute(portTest);
            return;
        }
        for (int i = scanPortState.startPort; i <= scanPortState.endPort; i++) {
            PortTest portTest = new PortTest();
            portTest.setConfig(scanPortState, scanPortResult);
            portTest.setTestPort(i);
            executorService.execute(portTest);
        }
    }

    public void breakThread() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
