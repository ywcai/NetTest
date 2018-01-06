package ywcai.ls.mobileutil.tools.Station.presenter.inf;

import com.github.mikephil.charting.charts.LineChart;

import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.service.StationService;

/**
 * Created by zmy_11 on 2017/11/25.
 */

public interface MainStationActionInf {


    void startWork();

    void saveLogLocal();

    void saveLogRemote();

    void clearTask();

    void selectFlexButton(int pos);

    void unRegPhoneStateListener();
}
