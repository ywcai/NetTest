package ywcai.ls.mobileutil.tools.Wifi.presenter.inf;

import java.util.List;

import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;

public interface UpdateFragmentOneInf {
    void loadChannelTagStatus();

    void loadLockAndSaveVisible();

    void loadLockBtnStatus();

    void loadTaskBtnStatus();

    void loadSignalChangeData(List<WifiEntry> allList, int[] channelSum);

    void addTaskEnd(String str,boolean success);

    void showSelectEntryInfo(WifiEntry wifiEntry);
}
