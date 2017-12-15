package ywcai.ls.mobileutil.tools.ScanPort.presenter.inf;

/**
 * Created by zmy_11 on 2017/12/15.
 */

public interface ScanPortActionInf {
    void startScanPort();

    void stopScanPort();

    void scanPortEnd();

    void addScanIp(String targetIp);

    void addScanPortRange(int start, int end);
}
