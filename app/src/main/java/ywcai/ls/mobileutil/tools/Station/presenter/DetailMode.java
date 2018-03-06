package ywcai.ls.mobileutil.tools.Station.presenter;

import java.util.HashMap;


import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;

/**
 * Created by zmy_11 on 2017/11/26.
 */

public class DetailMode {

    public void refreshInfo(HashMap<String, Integer> cells, HashMap<String, Integer> signals) {
        if (cells != null) {
            sendMsgShowCellRealInfo(cells);
        }
        if (signals != null) {
            sendMsgShowSignalRealInfo(signals);
        }
    }

    private void sendMsgShowCellRealInfo(HashMap<String, Integer> cells) {
        MsgHelper.sendEvent(GlobalEventT.station_refresh_cell_log_info, "", cells);
    }

    private void sendMsgShowSignalRealInfo(HashMap<String, Integer> signals) {
        MsgHelper.sendEvent(GlobalEventT.station_refresh_signal_log_info, "", signals);
    }


}
