package ywcai.ls.mobileutil.tools.Wifi.presenter.inf;

import java.util.List;

import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;

/**
 * Created by zmy_11 on 2017/11/13.
 */

public interface UpdateFragmentThreeInf {
    void refreshChannelCurrent(int[] channelSum);
    void refreshChannelRecord(int[] channelSum);
    void refreshChannelForPieChart(int[] channelSum);
}

