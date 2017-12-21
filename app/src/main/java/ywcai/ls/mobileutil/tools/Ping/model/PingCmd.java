package ywcai.ls.mobileutil.tools.Ping.model;

import java.util.ArrayList;
import java.util.List;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.Ping;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.tools.Ping.presenter.inf.PingProcessInf;


public class PingCmd implements Runnable {
    private PingState pingState;
    private PingProcessInf pingProcessInf;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private List<Float> listResult;

    public PingCmd(PingProcessInf _pingProcessInf, List _list,PingState pingState) {
        if (_list != null) {
            listResult = _list;
        }
        this.pingState=pingState;
        pingProcessInf = _pingProcessInf;
    }

    public void PingTest() {
        Ping ping = new Ping();
        float result = ping.pingCmd(pingState.ipAddress);
        updateState(result);
    }

    @Override
    public void run() {
        PingTest();
    }

    private void updateState(float y) {
        PingState temp = new PingState();
        synchronized (pingState) {
            listResult.add(y);
            pingState.send = listResult.size();
            pingState.nowDelay = y;
            pingState.max = pingState.max <= y ? y : pingState.max;
            pingState.min = (pingState.min <= 0 && y > 0) ? y : (y <= pingState.min && y > 0) ? y : pingState.min;
            pingState.per = (float) (pingState.loss * 10000 / pingState.send) / 100;
            if (y < 0) {
                pingState.loss++;
            }
            if (y >= 0) {
                pingState.average =
                        pingState.send == pingState.loss ? 0 :
                                (float) Math.round((pingState.average * (pingState.send - 1) + y) * 100 / pingState.send) / 100;
            }
            temp.copy(pingState);
            if (temp.send <= temp.packageCount) {
                cacheProcess.setPingResult(AppConfig.INDEX_PING + "-" + pingState.startTime, listResult);
                pingProcessInf.refreshRunningTask(temp);
            }
            if (temp.send == temp.packageCount) {
                pingProcessInf.autoCompleteTask();
            }
        }
    }
}
