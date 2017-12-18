package ywcai.ls.mobileutil.tools.ScanPort.presenter.inf;

/**
 * Created by zmy_11 on 2017/12/15.
 */

public interface ScanPortActionInf {

    void clickOperatorBtn();

    void addScanTask(String ip, String startText, String endText);

    void recoveryUI();

    void waitService();
}
