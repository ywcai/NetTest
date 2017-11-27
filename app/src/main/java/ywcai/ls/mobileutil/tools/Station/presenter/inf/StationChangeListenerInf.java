package ywcai.ls.mobileutil.tools.Station.presenter.inf;

import java.util.HashMap;
import java.util.List;

import ywcai.ls.mobileutil.tools.Station.model.StationEntry;


/**
 * Created by zmy_11 on 2017/11/25.
 */

public interface StationChangeListenerInf {
    void stationDataChange( HashMap<String, Integer> cellList, HashMap<String, Integer> signalList);
    void resetCellListener();
}
