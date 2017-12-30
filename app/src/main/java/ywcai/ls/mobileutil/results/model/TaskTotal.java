package ywcai.ls.mobileutil.results.model;

import ywcai.ls.mobileutil.global.cfg.AppConfig;


public class TaskTotal {
    //saveCount 在保存本地数据索引时被在CacheProcess对象中更新
    public int runCount = 0, waitProcessCount = 0, savedCount = 0;
    public int[] state = new int[10];

    public void autoCount() {
        runCount = 0;
        waitProcessCount = 0;
        countPingTask();
        countWifiTask();
        countScanPortTask();
        countSpeedTestTask();
    }

    private void countPingTask() {
        if (state[AppConfig.INDEX_PING] >= 1 && state[AppConfig.INDEX_PING] <= 99) {
            runCount++;
        }
        if (state[AppConfig.INDEX_PING] == 100) {
            waitProcessCount++;
        }
    }

    private void countWifiTask() {
        runCount += state[AppConfig.INDEX_WIFI];
    }

    private void countScanPortTask() {
        if (state[AppConfig.INDEX_PORT] >= 1 && state[AppConfig.INDEX_PORT] <= 99) {
            runCount++;
        }
        if (state[AppConfig.INDEX_PORT] == 100) {
            waitProcessCount++;
        }
    }

    private void countSpeedTestTask() {
        if (state[AppConfig.INDEX_SPEED] == 100) {
            waitProcessCount++;
        }
    }

}
