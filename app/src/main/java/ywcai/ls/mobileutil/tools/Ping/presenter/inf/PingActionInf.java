package ywcai.ls.mobileutil.tools.Ping.presenter.inf;

/**
 * Created by zmy_11 on 2017/10/3.
 */

public interface PingActionInf {
    void activityResume();

    void clickBtnTest(String ip);

    void clickBtnPauseAndResume();

    void changeBarPackageNumber(int progress);

    void changeBarThreadNumber(int progress);

    void changeEditText(String s);

    void clickBtnSaveLocal();

    void clickBtnSaveRemote();

    void clickBtnSaveClear();

    void clickBtnSaveCancal();

    void clickBtnFloating();

}
