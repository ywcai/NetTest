package ywcai.ls.mobileutil.tools.Wifi.presenter.inf;

import java.util.List;

import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;

/**
 * Created by zmy_11 on 2017/11/13.
 */

public interface UpdateFragmentTwoInf {
    void refreshList(List<WifiEntry> allList);
    void refreshChart();
    void popOperatorMenu();
    void updateItemBtn(int pos,boolean isShowInChart);
}

