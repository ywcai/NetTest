package ywcai.ls.mobileutil.tools.Ping.presenter.inf;

import ywcai.ls.mobileutil.service.PingService;
import ywcai.ls.mobileutil.tools.Ping.model.PingState;

/**
 * Created by zmy_11 on 2017/10/4.
 */

public interface PingProcessInf {
    void refreshRunningTask(PingState pingState);
    void manualStartTask(PingService pingService);
    void autoRecoveryTask(PingService pingService);
    void manualStopTask(PingActionInf actionInf);
    void autoCompleteTask();
    void manualPauseTask(PingActionInf actionInf);
}
