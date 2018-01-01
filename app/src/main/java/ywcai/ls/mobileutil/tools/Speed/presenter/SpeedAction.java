package ywcai.ls.mobileutil.tools.Speed.presenter;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.util.statics.MyTime;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.TaskTotal;
import ywcai.ls.mobileutil.tools.Speed.model.SpeedState;
import ywcai.ls.mobileutil.tools.Speed.model.SpeedTest;
import ywcai.ls.mobileutil.tools.Speed.presenter.inf.SpeedActionInf;

public class SpeedAction implements SpeedActionInf {

    SpeedState speedState;
    SpeedTest speedTest;
    CacheProcess cacheProcess = CacheProcess.getInstance();

    public SpeedAction() {
        speedState = cacheProcess.getSpeedState();
        speedTest = new SpeedTest(speedState);
        if (speedState.running == 2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    speedTest.processComplete();
                    sendMsgRefreshRealSpeed();
                }
            }).start();

        } else {
            sendMsgReady();
        }
    }

    @Override
    public void clickStartBtn() {
        speedTest.test();
    }

    @Override
    public void clickPopMenu() {
        //do no thing;
    }

    @Override
    public void clickSaveForLocal() {
        saveLocal();
        speedState.reset();
        cacheProcess.setSpeedState(speedState);
        TaskTotal taskTotal = cacheProcess.getCacheTaskTotal();
        taskTotal.state[AppConfig.INDEX_SPEED] = 0;
        taskTotal.autoCount();
        cacheProcess.setCacheTaskTotal(taskTotal);
        sendMsgReady();
    }


    @Override
    public void clickSaveForRemote() {
        saveRemote();
//        speedState.reset();
//        sendMsgReady();
    }


    @Override
    public void clickReset() {
        speedState.reset();
        cacheProcess.setSpeedState(speedState);
        TaskTotal taskTotal = cacheProcess.getCacheTaskTotal();
        taskTotal.state[AppConfig.INDEX_SPEED] = 0;
        taskTotal.autoCount();
        cacheProcess.setCacheTaskTotal(taskTotal);
        sendMsgReady();
    }


    @Override
    public void cancalOperator() {
        //do nothing
    }

    @Override
    public void closeActivity() {
        speedTest.breakThread();
    }


    private void saveLocal() {
        LogIndex logIndex = new LogIndex();
        logIndex.remarks = speedState.speedResult;
        logIndex.cacheTypeIndex = AppConfig.INDEX_SPEED;
        logIndex.logTime = MyTime.getDetailTime();
        logIndex.aliasFileName = "网络测速";
        logIndex.cacheFileName = "null";
        cacheProcess.addCacheLogIndex(logIndex);
        sendMsgSnackBar("本地保存成功!", true);
    }

    private void saveRemote() {
        sendMsgSnackBar("Sorry,暂不支持云端保存!", false);
    }

    private void sendMsgReady() {
        MsgHelper.sendStickEvent(GlobalEventT.speed_set_ready, "", null);
    }

    private void sendMsgSnackBar(String tip, boolean success) {
        MsgHelper.sendStickEvent(GlobalEventT.speed_set_snack_tip, tip, success);
    }

    private void sendMsgRefreshRealSpeed() {
        MsgHelper.sendStickEvent(GlobalEventT.speed_yibiao_read_data, "", speedState.realSpeed);
    }


}
