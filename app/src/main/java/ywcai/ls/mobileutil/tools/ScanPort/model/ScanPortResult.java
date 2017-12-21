package ywcai.ls.mobileutil.tools.ScanPort.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zmy_11 on 2017/12/15.
 */

public class ScanPortResult {
    //    public String protocol, scanIp;
    public List<Integer> openPorts = new ArrayList<>();
    public int maxCounts = -1, currentScanIndex = -1;//记录当前扫描的任务位置。以此可得到当前扫描的进

    public void reset() {
        openPorts.clear();
        maxCounts = -1;
        currentScanIndex = -1;
    }

}
