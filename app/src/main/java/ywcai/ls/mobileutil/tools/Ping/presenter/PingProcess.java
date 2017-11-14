package ywcai.ls.mobileutil.tools.Ping.presenter;

import android.view.View;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.LsThreadFactory;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.service.PingService;
import ywcai.ls.mobileutil.tools.Ping.model.PingCmd;
import ywcai.ls.mobileutil.tools.Ping.model.PingState;
import ywcai.ls.mobileutil.tools.Ping.presenter.inf.PingActionInf;
import ywcai.ls.mobileutil.tools.Ping.presenter.inf.PingProcessInf;


public class PingProcess implements PingProcessInf {
    ExecutorService executorService = null;
    CacheProcess cacheProcess = CacheProcess.getInstance();
    PingService pingService;

    @Override
    public void refreshRunningTask(PingState pingState) {
        cacheProcess.setCachePingState(pingState);
        MsgHelper.sendEvent(GlobalEventT.ping_update_chart_point, pingState.send + "", pingState.nowDelay);
        MsgHelper.sendEvent(GlobalEventT.ping_update_chart_desc, "", pingState);
        pingService.setTaskProgress(pingState.send,pingState.packageCount);
    }

    @Override
    public void manualStartTask(PingService _pingService) {
        pingService=_pingService;
        PingState pingState = cacheProcess.getCachePingState();
        pingState.isAutoRecovery = true;//非常重要的标识，标识已任务已经被手动开启过。.
        cacheProcess.setCachePingState(pingState);
        executeTask(pingState);
    }

    @Override
    public void autoRecoveryTask(PingService _pingService) {
        pingService=_pingService;
        if (executorService == null) {
            PingState pingState = cacheProcess.getCachePingState();
            executeTask(pingState);
        }
    }

    @Override
    public void manualStopTask(PingActionInf actionInf) {
        if (executorService != null) {
            executorService.shutdownNow();
            while (!executorService.isTerminated()) {
            }
        }
        PingState pingState = cacheProcess.getCachePingState();
        pingState.setStopState();
        cacheProcess.setCachePingState(pingState);
        actionInf.activityResume();
        pingService.setTaskCompleteTip(2);
    }

    @Override
    public void autoCompleteTask() {
        PingState pingState = cacheProcess.getCachePingState();
        pingState.setStopState();
        cacheProcess.setCachePingState(pingState);
        MsgHelper.sendEvent(GlobalEventT.ping_set_form_free, "", null);
        MsgHelper.sendEvent(GlobalEventT.ping_set_float_btn_visible, "", View.VISIBLE);
        pingService.setTaskCompleteTip(0);
    }

    //恢复和重启一个任务是同一个方法，只是读取到的缓存不同
    @Override
    public void manualPauseTask(PingActionInf actionInf) {
        if (executorService != null) {
            executorService.shutdownNow();
            while (!executorService.isTerminated()) {
            }
        }
        PingState pingState = cacheProcess.getCachePingState();
        pingState.setPauseState();
        cacheProcess.setCachePingState(pingState);
        actionInf.activityResume();
        pingService.setTaskCompleteTip(1);

    }

    private void executeTask(PingState pingState) {
        LsThreadFactory threadFactory = new LsThreadFactory();
        executorService = Executors.newFixedThreadPool(pingState.threadCount, threadFactory);
        List list = cacheProcess.getPingResult(AppConfig.INDEX_PING +"-"+ pingState.startTime);
        PingCmd pingCmd = new PingCmd(this, list);
        int pos = 0;
        if (list != null) {
            pos = list.size();
        }
        for (int i = pos; i < pingState.packageCount; i++) {
            executorService.execute(pingCmd);
        }
    }
}
