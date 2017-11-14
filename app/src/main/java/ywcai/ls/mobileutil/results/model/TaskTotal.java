package ywcai.ls.mobileutil.results.model;

import ywcai.ls.mobileutil.global.cfg.AppConfig;

/**
 * Created by zmy_11 on 2017/10/9.
 */

public class TaskTotal {
    public int runCount = 0, waitProcessCount = 0, savedCount = 0;
    public int[] state=new int[10];
    public void autoCount()
    {
        runCount=0;
        waitProcessCount=0;
        countPingTask();
        countWifiTask();
    }
    private void countPingTask()
    {
            if(state[AppConfig.INDEX_PING]>=1&&state[AppConfig.INDEX_PING]<=99)
            {
                runCount++;
            }
            if(state[AppConfig.INDEX_PING]==100)
            {
                waitProcessCount++;
            }
    }
    private void countWifiTask()
    {
        runCount+=state[AppConfig.INDEX_WIFI];
    }

}
