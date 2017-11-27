package ywcai.ls.mobileutil.tools.Station.presenter;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;


public class NormalMode {
    StationEntry currentEntry;
    private List<StationEntry> stationEntries = new ArrayList<>();
    private List<Entry> entryList = new ArrayList<>();

    public NormalMode(StationEntry stationEntry) {
        this.currentEntry = stationEntry;
    }

    public void saveRecordForChart() {
//        stationEntries= CacheProcess.getInstance().getStationRecord();

    }

    public void refreshInfo(HashMap<String, Integer> cells, HashMap<String, Integer> signals) {

        if (cells != null) {
            if (currentEntry.netTypeName.equals(currentEntry.UNKNOWN)) {
                currentEntry.cid = 0;
                currentEntry.lac = 0;
                currentEntry.rsp = 0;
            } else {
                if (cells.get("mCid") != null) {
                    currentEntry.cid = cells.get("mCid");
                }
                if (cells.get("mLac") != null) {
                    currentEntry.lac = cells.get("mLac");
                }
            }
            sendMsgShowRealInfo();
        }
        if (signals != null) {
            if (signals.get(currentEntry.netTypeName) != null) {
                currentEntry.rsp = signals.get(currentEntry.netTypeName);
            }
            stationEntries.add(currentEntry);
            if (currentEntry.rsp >= -1) {
                entryList.add(new Entry(stationEntries.size() - 1, -160));
            } else {
                entryList.add(new Entry(stationEntries.size() - 1, currentEntry.rsp));
            }
            sendMsgShowRealInfo();
            sendMsgRefreshLineChart();
        }

    }

    //刷新常规页面数据
    private void sendMsgShowRealInfo() {
        MsgHelper.sendEvent(GlobalEventT.station_refresh_entry_info, "", currentEntry);
    }

    private void sendMsgRefreshLineChart() {
        MsgHelper.sendEvent(GlobalEventT.station_refresh_chart_entry_record, "", entryList);

    }

}
