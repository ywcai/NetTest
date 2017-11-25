package ywcai.ls.mobileutil.tools.Wifi.presenter.inf;

import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by zmy_11 on 2017/11/13.
 */

public interface UpdateFragmentFourInf {


    //    void refreshChannelLevel(int[] channelSum);
    void refreshChannelLineRecord(int[] channelSum);

    void switchLineChartSelect();

    void saveBitMap(LineChart wifiChannelRecord);
}

