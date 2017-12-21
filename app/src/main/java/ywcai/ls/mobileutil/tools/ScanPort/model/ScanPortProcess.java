package ywcai.ls.mobileutil.tools.ScanPort.model;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsListTransfer;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.util.statics.MyTime;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.TaskTotal;
import ywcai.ls.mobileutil.service.ScanPortService;

public class ScanPortProcess {

    private ScanPortState scanPortState;
    private ScanPortResult scanPortResult;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private ScanPortService scanPortService;

    public ScanPortProcess() {
        this.scanPortState = cacheProcess.getScanPortState();
        this.scanPortResult = cacheProcess.getScanPortResult();

    }

    public void setScanPortService(ScanPortService scanPortService) {
        this.scanPortService = scanPortService;
        scanPortService.recoveryExecuteTask(scanPortState, scanPortResult);
        isNeedRecoveryTask();
    }

    /*
    如果当前任务状态是运行状态，测需要进一步检测是否真正有后台TASK执行。
    如果检测到有TASK执行，则不处理UI
    如果检测到无TASK执行，则根据保存的任务参数恢复任务
     */
    private void isNeedRecoveryTask() {
        //只要没有关闭APP退出，Service始终将任务执行完。
        //如果任务没有执行完退出后又重新打开APP，则直接显示上一次执行到中途的结果。
        if (scanPortState.scanTaskState == 1) {
            scanPortService.startTask();
        }
    }

    public void selectProcess() {
        if (scanPortService == null) {
            return;
        }
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
        reSetWorkConfig();
        sendMsgTopCardNew();
    }

    private void reSetWorkConfig() {
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
    }


    /*
    Core process
     */
    private void startScanTask() {
        scanPortState.scanTaskState = 1;
        cacheProcess.setScanPortState(scanPortState);
        sendMsgTopCardRun();
        scanPortService.resetThread(scanPortState, scanPortResult);
        scanPortService.startTask();
    }

    private void stopScanTask() {
        scanPortService.stopExecuteTask();
        scanPortState.scanTaskState = 2;
        cacheProcess.setScanPortState(scanPortState);
        sendMsgTopCardEnd();
    }

    private void processResult() {
        saveScanLog();
        scanPortState.reset();
        cacheProcess.setScanPortState(scanPortState);
        scanPortResult.reset();
        cacheProcess.setScanPortResult(scanPortResult);
        sendMsgTopCardNone();
        sendMsgPopBottomMsg("结果已在本地保存!");
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
                + " 端口明细:" + LsListTransfer.IntegerToString(scanPortResult.openPorts);
        cacheProcess.addCacheLogIndex(logIndex);
        TaskTotal taskTotal = cacheProcess.getCacheTaskTotal();
        taskTotal.state[AppConfig.INDEX_PORT] = 0;
        taskTotal.autoCount();
        cacheProcess.setCacheTaskTotal(taskTotal);
    }

    /*
    refresh the ui method
     */

    private void sendMsgTopCardNone() {
        MsgHelper.sendStickEvent(GlobalEventT.scan_port_set_card_run_info_none, "", null);
    }

    //刷新顶部的card栏任务信息，并显示开始按钮
    private void sendMsgTopCardNew() {
        MsgHelper.sendStickEvent(GlobalEventT.scan_port_set_card_run_info_new, "", scanPortState);
    }

    private void sendMsgTopCardRun() {
        MsgHelper.sendStickEvent(GlobalEventT.scan_port_set_card_run_info_run, "", scanPortState);
    }

    private void sendMsgTopCardEnd() {
        MsgHelper.sendStickEvent(GlobalEventT.scan_port_set_card_run_info_end, "", scanPortState);
    }

    //刷更新雷达上的扫描结果，结果及百分比信息
    private void sendMsgMainRadarData() {
        MsgHelper.sendStickEvent(GlobalEventT.scan_port_recovery_radar_data, "", scanPortResult);
    }

    private void sendMsgPopBottomMsg(String tip) {
        MsgHelper.sendEvent(GlobalEventT.scan_port_show_bottom_msg, tip, null);
    }


}
