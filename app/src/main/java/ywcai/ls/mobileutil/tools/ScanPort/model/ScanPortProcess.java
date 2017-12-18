package ywcai.ls.mobileutil.tools.ScanPort.model;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.ListToString;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.util.statics.MyTime;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.TaskTotal;

public class ScanPortProcess {

    private ScanPortState scanPortState;
    private ScanPortResult scanPortResult;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private ScanTaskExecute scanTaskExecute;

    public ScanPortProcess() {
        this.scanPortState = cacheProcess.getScanPortState();
        this.scanPortResult = cacheProcess.getScanPortResult();
        //如果是新构造的业务，说明原来的service已经中断，需要自动从缓存中重新恢复任务
        recoveryStartTask();
    }


    public void selectProcess() {
        switch (scanPortState.scanTaskState) {
            case 0:
                startScanTask();
                break;
            case 1:
                stopScanTask();
                break;
            case 2:
                processResult();
                break;
        }
    }

    /*
    response resume activity event
     */
    public void recoveryUiState() {
        switch (scanPortState.scanTaskState) {
            case -1:
                sendMsgTopCardNone();
                break;
            case 0:
                sendMsgTopCardNew();
                break;
            case 1:
                //同时恢复雷达的最新数据
                sendMsgTopCardRun();
                sendMsgMainRadarData();
                break;
            case 2:
                //同时恢复雷达的最新数据
                sendMsgTopCardEnd();
                sendMsgMainRadarData();
                break;
        }
    }


    public void addScanPortTask(String ip, String startText, String endText) {
        scanPortState.targetIp = ip;
        int start = 0, end = 0;
        if (!startText.equals("")) {
            start = Integer.parseInt(startText);
        }
        if (!endText.equals("")) {
            end = Integer.parseInt(endText);
        }
        if (start > end) {
            scanPortState.startPort = end;
            scanPortState.endPort = start;
        } else {
            scanPortState.startPort = start;
            scanPortState.endPort = end;
        }
        scanPortState.scanTaskState = 0;
        cacheProcess.setScanPortState(scanPortState);
        scanPortResult.currentScanIndex = 0;
        scanPortResult.openPorts.clear();
        if (scanPortState.startPort == 0) {
            scanPortResult.maxCounts = 1;
        } else {
            scanPortResult.maxCounts = scanPortState.endPort - scanPortState.startPort + 1;
        }
        cacheProcess.setScanPortResult(scanPortResult);
        sendMsgTopCardNew();
    }

    /*
    Core process
     */
    private void startScanTask() {
        scanPortState.scanTaskState = 1;
        cacheProcess.setScanPortState(scanPortState);
        sendMsgTopCardRun();
        scanTaskExecute = new ScanTaskExecute(scanPortState, scanPortResult);
        scanTaskExecute.startScanTask();
    }

    private void stopScanTask() {
        if (scanTaskExecute != null) {
            scanTaskExecute.breakThread();
        }
        scanPortState.scanTaskState = 2;
        cacheProcess.setScanPortState(scanPortState);
        sendMsgTopCardEnd();
    }

    private void processResult() {
        saveScanLog();
        scanPortState = new ScanPortState();
        cacheProcess.setScanPortState(scanPortState);
        scanPortResult = new ScanPortResult();
        cacheProcess.setScanPortResult(scanPortResult);
        sendMsgTopCardNone();
        sendMsgPopBottomMsg("结果已在本地保存");
    }


    private void saveScanLog() {
        //数据直接在索引中保存了，不单独存缓存日志
        LogIndex logIndex = new LogIndex();
        logIndex.cacheTypeIndex = AppConfig.INDEX_PORT;
        logIndex.logTime = MyTime.getDetailTime();
        logIndex.cacheFileName = logIndex.logTime;
        logIndex.aliasFileName = "端口扫描";
        logIndex.remarks = "IP:" + scanPortState.targetIp
                + " PORT:" + scanPortState.startPort
                + "-" + scanPortState.endPort
                + " 扫描数量:" + scanPortResult.maxCounts
                + " 打开数量:" + scanPortResult.openPorts.size()
                + " 端口明细:" + ListToString.IntegerToString(scanPortResult.openPorts);
        cacheProcess.addCacheLogIndex(logIndex);
        TaskTotal taskTotal = cacheProcess.getCacheTaskTotal();
        taskTotal.state[AppConfig.INDEX_PORT] = 0;
        taskTotal.autoCount();
        cacheProcess.setCacheTaskTotal(taskTotal);
    }

    //如果是重新打开了一个对象，则先更新UI，再根据当前的状态确认是否重新建立工作线程
    private void recoveryStartTask() {
        recoveryUiState();
        //recoveryWorkTask();
    }



    /*
    refresh the ui method
     */

    private void sendMsgTopCardNone() {
        MsgHelper.sendEvent(GlobalEventT.scan_port_set_card_run_info_none, "", null);
    }

    //刷新顶部的card栏任务信息，并显示开始按钮
    private void sendMsgTopCardNew() {
        MsgHelper.sendEvent(GlobalEventT.scan_port_set_card_run_info_new, "", scanPortState);
    }

    private void sendMsgTopCardRun() {
        MsgHelper.sendEvent(GlobalEventT.scan_port_set_card_run_info_run, "", scanPortState);
    }

    private void sendMsgTopCardEnd() {
        MsgHelper.sendEvent(GlobalEventT.scan_port_set_card_run_info_end, "", scanPortState);
    }

    //刷更新雷达上的扫描结果，结果及百分比信息
    private void sendMsgMainRadarData() {
        MsgHelper.sendEvent(GlobalEventT.scan_port_recovery_radar_data, "", scanPortResult);
    }

    private void sendMsgPopBottomMsg(String tip) {
        MsgHelper.sendEvent(GlobalEventT.scan_port_show_bottom_msg, tip, null);
    }

}
