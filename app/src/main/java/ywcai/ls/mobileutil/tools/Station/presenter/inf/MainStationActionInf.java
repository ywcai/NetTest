package ywcai.ls.mobileutil.tools.Station.presenter.inf;

import com.github.mikephil.charting.charts.LineChart;

import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.service.StationService;

/**
 * Created by zmy_11 on 2017/11/25.
 */

public interface MainStationActionInf {

    //等待
    void startWork();

//    void isDoubleCard();
//
//    void isSupportOs();
//
//    void addSingleListener(boolean isSupportDouble);
//
//    void addCellListener(boolean isSupportDouble);

    void addTask();//设置为前台服务

    void removeTask();//退出前台服务

    void popOperatorMenu(boolean isShow);

    void saveLogLocal();

    void saveLogRemote();

    void clearTask();

    void saveBitmap(LineChart lineChart);

    void setToDtMode(boolean isDtMode);//进入路测模式？。预留

    void sendToRemoteReal(boolean isSend);//实时传输当前测试的数据。预留


}
