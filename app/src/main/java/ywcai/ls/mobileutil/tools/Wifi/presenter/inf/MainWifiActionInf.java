package ywcai.ls.mobileutil.tools.Wifi.presenter.inf;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;

/**
 * Created by zmy_11 on 2017/10/14.
 */

public interface MainWifiActionInf {
    void startWifiService();

    void lockWifi();

//    void setSelectMac(String mac);


    void setSelectEntry(WifiEntry wifiEntry);

    void addOrRemoveTask();

    void saveHighLightHolderView(Highlight[] hh);

    void set2d4G(boolean choose2d4G);

    void setChannelFilter(int index, boolean isSelect);

    void setAllTagSelectOrCancal(int[] allTagStatus);

    void saveLogForLocal(int pos);

    void saveLogForRemote(int pos);

    void clearLog(int pos);

    void saveAllLogForLocal();

    void saveAllLogForRemote();

    void clearAllLog();

    void clickTaskItem();

    void showChartLine(int popTaskPos);

    void saveBitmap(LineChart wifiChannelRecord);
}
