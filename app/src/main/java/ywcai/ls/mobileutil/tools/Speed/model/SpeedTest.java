package ywcai.ls.mobileutil.tools.Speed.model;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.LsThreadFactory;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.results.model.TaskTotal;


public class SpeedTest {
    SpeedState speedState;
    ExecutorService executorService;
    CacheProcess cacheProcess = CacheProcess.getInstance();

    public SpeedTest(SpeedState speedState) {
        this.speedState = speedState;
        LsThreadFactory lsThreadFactory = new LsThreadFactory();
        executorService = Executors.newFixedThreadPool(30, lsThreadFactory);
    }

    public void test() {
        speedState.start();
        cacheProcess.setSpeedState(speedState);
        sendMsgLoadThread();
        requestWeb();
        downloadTestForApk();
        refreshUi();
    }

    public void breakThread() {
        if (speedState.running == 1) {
            speedState.reset();
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }
        cacheProcess.setSpeedState(speedState);
    }

    //测试几大门户网站掉包率
    private void requestWeb() {
        //访问10大门户站首页，每个1次，30线程进行处理
        for (int i = 0; i < speedState.readMaxCount; i++) {
            DownRunnable runnable = new DownRunnable(speedState, i);
            executorService.execute(runnable);
        }
    }

    //大文件下载，暂时没有使用
    private void downloadTestForApk() {
        //实时保存当前下载的测试数据。
        //当前下载数据，当前时间，平均速度，
        for (int i = 0; i < speedState.downMaxCount; i++) {
            DownRunnable runnable = new DownRunnable(speedState, i);
            executorService.execute(runnable);
        }
    }


    private void refreshUi() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (speedState.running == 1) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (speedState.currentComplete >= 1) {
                        //取消不确定进度条，显示明确进度条。
                        sendMsgLoadTask();
                        speedState.useTime = (int) (Calendar.getInstance().getTimeInMillis() - speedState.startTime);
                        speedState.realSpeed = (float) ((int) (speedState.payloadSize * 8 / speedState.useTime / 10) / 100.00);
                        int progress = speedState.currentComplete * 100 / (speedState.readMaxCount + speedState.downMaxCount);
                        processRun(progress);
                    }
                }
                if (speedState.running == 2) {
                    processComplete();
                    return;
                }
            }
        }).start();
    }

    private void processRun(int progress) {
        sendMsgRefreshProgress(progress);
        sendMsgRefreshRealSpeed();
    }

    public void processComplete() {
        speedState.complete();
        doTaskLog();
        sendMsgComplete();
    }

    private void doTaskLog() {
        TaskTotal taskTotal = cacheProcess.getCacheTaskTotal();
        taskTotal.state[AppConfig.INDEX_SPEED] = 100;
        taskTotal.autoCount();
        cacheProcess.setCacheTaskTotal(taskTotal);
    }

    private void sendMsgRefreshProgress(int progress) {
        MsgHelper.sendStickEvent(GlobalEventT.speed_set_progress, "", progress);
    }

    private void sendMsgRefreshRealSpeed() {
        MsgHelper.sendStickEvent(GlobalEventT.speed_yibiao_read_data, "", speedState.realSpeed);
    }

    private void sendMsgComplete() {
        MsgHelper.sendStickEvent(GlobalEventT.speed_set_complete, speedState.speedResult, null);
    }

    private void sendMsgLoadThread() {
        MsgHelper.sendStickEvent(GlobalEventT.speed_set_running_wait_task, "", null);
    }

    private void sendMsgLoadTask() {
        MsgHelper.sendStickEvent(GlobalEventT.speed_set_running_receive_task, "", null);
    }

}
