package ywcai.ls.mobileutil.tools.Ping.presenter;

import android.view.View;

import java.util.List;

import rx.functions.Action1;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.InStallService;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.http.present.HttpRequest;
import ywcai.ls.mobileutil.http.present.HttpRequestInf;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.PingService;
import ywcai.ls.mobileutil.tools.Ping.model.PingState;
import ywcai.ls.mobileutil.tools.Ping.presenter.inf.PingActionInf;

public class PingAction implements PingActionInf {
    CacheProcess cacheProcess = CacheProcess.getInstance();
    private PingService pingService;
    //Connection内部已经阻塞过了，放心使用。
    LsConnection lsConnection = new LsConnection(new Action1() {
        @Override
        public void call(Object o) {
            if (o != null) {
                pingService = (PingService) o;
                InStallService.waitService(pingService);
                invokeTask();
            } else {
                pingService = null;
            }
        }
    });

    private void setBar1Size(int packageCount) {
        MsgHelper.sendEvent(GlobalEventT.ping_set_bar_size_package, "", packageCount);
    }

    private void setBar2Size(int threadCount) {
        MsgHelper.sendEvent(GlobalEventT.ping_set_bar_size_thread, "", threadCount);
    }

    private void repairBaseLine(int packageCount) {
        MsgHelper.sendEvent(GlobalEventT.ping_set_chart_data_size, "", packageCount);
    }

    private void setInputText(String ipAddress) {
        MsgHelper.sendEvent(GlobalEventT.ping_set_input_text_ip, "", ipAddress);
    }

    private void repairLine(String startTime) {
        List<Float> list = cacheProcess.getPingResult(AppConfig.INDEX_PING + "-" + startTime);
        if (list != null) {
            MsgHelper.sendEvent(GlobalEventT.ping_repair_chart_line, "", list);//修复曲线数据
        }
    }

    private void popProcessDialog() {
        MsgHelper.sendEvent(GlobalEventT.ping_pop_operator_dialog, "", null);//弹出任务结果处理框
    }

    private void setTaskFree() {
        MsgHelper.sendEvent(GlobalEventT.ping_set_form_free, "", null);//更新Form为没有任务状态
    }

    private void setTaskRun() {
        MsgHelper.sendEvent(GlobalEventT.ping_set_form_busy, "", null);//更新Form为任务运行中状态
    }

    private void setTaskPause() {
        MsgHelper.sendEvent(GlobalEventT.ping_set_form_pause, "", null);//更新Form为任务暂停状态
    }

    private void drawDesc(PingState pingState) {
        MsgHelper.sendEvent(GlobalEventT.ping_update_chart_desc, "", pingState);//更新图表描述信息
    }

    private void popLoadingWindow(String tip) {
        MsgHelper.sendEvent(GlobalEventT.ping_pop_loading_dialog, tip, null);
    }

    private void closeLoadingWindow(String tip) {
        MsgHelper.sendEvent(GlobalEventT.ping_close_loading_dialog, tip, null);
    }

    private void setFloatBtnVisible(int visible) {
        MsgHelper.sendEvent(GlobalEventT.ping_set_float_btn_visible, "", visible);
    }


    @Override
    public void activityResume() {
        PingState initState = cacheProcess.getCachePingState();
        if (initState == null) {
            initState = new PingState();
            cacheProcess.setCachePingState(initState);
        }
        repairBaseData(initState);
        //有任务，正在运行
        if (initState.isRunning && (!initState.isPause)) {
            repairLine(initState.startTime);
            setTaskRun();
            setFloatBtnVisible(View.INVISIBLE);
            startPingService();
        }
        //有任务，但已暂停
        if (initState.isRunning && initState.isPause) {
            repairLine(initState.startTime);
            setTaskPause();//显示暂停的按钮
            setFloatBtnVisible(View.INVISIBLE);
        }
        //任务完成，但结果未处理
        if (!initState.isRunning && !initState.isProcessResult) {
            repairLine(initState.startTime);
            setTaskFree();
            setFloatBtnVisible(View.VISIBLE);
        }
        //无任务，无结果，空闲状态
        if (!initState.isRunning && initState.isProcessResult) {
            setTaskFree();//更新Form为没有任务状态
            setFloatBtnVisible(View.INVISIBLE);
        }
    }


    @Override
    public void clickBtnTest(String ip) {
        PingState initState = cacheProcess.getCachePingState();
        if (!initState.isRunning) {
            manualStart(ip);
        } else {
            manualStop();
        }
    }

    @Override
    public void clickBtnPauseAndResume() {
        PingState initState = cacheProcess.getCachePingState();
        if (!initState.isPause) {
            manualPause();
        } else {
            manualResume();
        }
    }

    private void manualResume() {
        PingState pingState = cacheProcess.getCachePingState();
        pingState.setResumeState();
        cacheProcess.setCachePingState(pingState);
        activityResume();
    }

    private void manualPause() {
        if (pingService != null) {
            pingService.pingProcessInf.manualPauseTask(this);
        }
    }

    @Override
    public void changeBarPackageNumber(int progress) {
        PingState initState = cacheProcess.getCachePingState();
        initState.packageCount = progress;
        cacheProcess.setCachePingState(initState);
        repairBaseLine(initState.packageCount);
        //如果有还未处理的数据，则修复数据曲线;
        repairLine(initState.startTime);
    }

    @Override
    public void changeBarThreadNumber(int progress) {
        PingState initState = cacheProcess.getCachePingState();
        initState.threadCount = progress;
        cacheProcess.setCachePingState(initState);
    }

    @Override
    public void changeEditText(String s) {
        PingState initState = cacheProcess.getCachePingState();
        initState.ipAddress = s;
        cacheProcess.setCachePingState(initState);
        drawDesc(initState);
    }

    @Override
    public void clickBtnSaveLocal() {
        PingState initState = cacheProcess.getCachePingState();
        addDataIndex(initState);//创建日志的索引
        resetChart(initState);
        List list = cacheProcess.getCacheLogIndex();
        closeLoadingWindow("数据成功保存到本地:");
    }

    @Override
    public void clickBtnSaveRemote() {
        //启动加载模态窗口
        popLoadingWindow("正在上传数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                PingState initState = cacheProcess.getCachePingState();
                HttpRequestInf http = new HttpRequest();
                if (http.updatePingResult()) {
                    deleteLastData(initState.startTime);
                    resetChart(initState);
                    closeLoadingWindow("功能开发中,敬请期待.上传失败！");
                } else {
                    closeLoadingWindow("上传云端失败");
                }

            }
        }).start();
    }

    @Override
    public void clickBtnSaveClear() {
        PingState initState = cacheProcess.getCachePingState();
        deleteLastData(initState.startTime);
        resetChart(initState);
    }

    @Override
    public void clickBtnSaveCancal() {
        //do nothing
        setFloatBtnVisible(View.VISIBLE);
    }

    @Override
    public void clickBtnFloating() {
        setFloatBtnVisible(View.INVISIBLE);
        popProcessDialog();
    }

    //如果需要运行任务，则启动后台任务并绑定他，调用任务处理进程处理。
    private void startPingService() {
        if (pingService == null) {
            InStallService.bindService(PingService.class, lsConnection);
        } else {
            invokeTask();
        }
    }

    private void invokeTask() {
        PingState pingState = cacheProcess.getCachePingState();
        if (!pingState.isAutoRecovery) {
            pingService.pingProcessInf.manualStartTask(pingService);
        } else {
            pingService.pingProcessInf.autoRecoveryTask(pingService);
        }
    }

    private void repairBaseData(PingState pingState) {
        setBar1Size(pingState.packageCount);
        setBar2Size(pingState.threadCount);
        repairBaseLine(pingState.packageCount);
        setInputText(pingState.ipAddress);
        drawDesc(pingState);
    }

    private void manualStart(String ip) {
        PingState pingState = cacheProcess.getCachePingState();
        cacheProcess.setPingResult(AppConfig.INDEX_PING + "-" + pingState.startTime, null);//清除上一次产生的结果数据，起始可以不自动清除，只是会造成日志文件无法被清理。
        pingState.setNewRunningState(ip);
        cacheProcess.setCachePingState(pingState);
        activityResume();
    }

    private void manualStop() {
        if (pingService != null) {
            pingService.pingProcessInf.manualStopTask(this);
        }
    }

    private void resetChart(PingState initState) {
        initState.setProcessEndState();//重置状态
        cacheProcess.setCachePingState(initState);//更新缓存
        repairBaseLine(initState.packageCount);//恢复基线宽度
        drawDesc(initState);//恢复描述信息
    }

    private void deleteLastData(String startTime) {
        //若在启动下一次任务前未处理这次任务的结果，系统将默认删除本次测试的数据结果，必须在处理之前运行删除，否则会找不到文件名字早就永久缓存为垃圾文件
        cacheProcess.setPingResult(AppConfig.INDEX_PING + "-" + startTime, null);
    }

    private void addDataIndex(PingState initState) {
        LogIndex logIndex = new LogIndex();
        logIndex.cacheTypeIndex = AppConfig.INDEX_PING;
        logIndex.cacheFileName = AppConfig.INDEX_PING + "-" + initState.startTime;
        logIndex.aliasFileName = logIndex.cacheFileName;
        logIndex.remarks = initState.getFormatMarks();
        logIndex.logTime = initState.startTime;
        cacheProcess.addCacheLogIndex(logIndex);
    }
}
