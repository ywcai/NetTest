package ywcai.ls.mobileutil.results.model;

import ywcai.ls.mobileutil.global.cfg.AppConfig;


public class TaskTotal {
    public int runCount = 0, waitProcessCount = 0, savedCount = 0;
    public int[] state = new int[10];

    public void autoCount() {
        runCount = 0;
        waitProcessCount = 0;
        countPingTask();
        countWifiTask();
        countScanPortTask();
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
}
