package ywcai.ls.mobileutil.tools.Station.presenter;

import com.github.mikephil.charting.data.Entry;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.util.statics.MyTime;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;
import ywcai.ls.mobileutil.tools.Station.model.StationState;


public class NormalMode {
    //超过500个后，remove最后一个。
    private StationState stationState;
    private StationEntry currentEntry;
    private List<StationEntry> stationEntries;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private int entryIndex = 0;

    public NormalMode(StationState stationState, StationEntry currentEntry) {
        this.stationState = stationState;
        this.currentEntry = currentEntry;
        stationEntries = cacheProcess.getStationRecord(stationState.logName);
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
            //保存数据到日志
            saveTempLog();
            sendMsgShowRealInfo();
        }
    }

    private void saveTempLog() {
        stationEntries.add(0, currentEntry);
        cacheProcess.setStationRecord(stationState.logName, currentEntry);
        recoveryChart();
    }


    public void saveLocal() {
        LogIndex logIndex = new LogIndex();
        logIndex.logTime = stationState.logName;
        logIndex.cacheTypeIndex = AppConfig.INDEX_STATION;
        logIndex.remarks = "基站信号测试数据";
        logIndex.aliasFileName = "日志别名";
        logIndex.cacheFileName = stationState.logName;
        cacheProcess.addCacheLogIndex(logIndex);
        //继续开始一个新的任务
        resetLog();
        sendMsgSnackBarTip("本地保存成功!", true);
    }

    public void saveRemote() {
        sendMsgSnackBarTip("开发中，暂不支持云端保存！", false);
    }

    public void clearLog() {
        //删除原有的
        cacheProcess.setStationRecord(stationState.logName, null);
        resetLog();
        sendMsgSnackBarTip("您成功重置了本地缓存的数据!", true);
    }

    private void resetLog() {
        stationEntries.clear();
        stationState.logName = MyTime.getDetailTime();
        sendMsgRefreshLineChart(null);
//        cacheProcess.setStationRecord(stationState.logName, currentEntry);
    }


    //单重新创建服务时 渲染UI。
    public void recoveryChart() {
        Observable.from(stationEntries)
                .map(new Func1<StationEntry, Entry>() {
                    @Override
                    public Entry call(StationEntry stationEntry) {
                        entryIndex++;
                        if (stationEntry.rsp >= -1) {
                            return new Entry(entryIndex - 1, -160);
                        } else {
                            return new Entry(entryIndex - 1, stationEntry.rsp);
                        }
                    }
                })
                .toList()
                .subscribe(new Action1<List<Entry>>() {
                    @Override
                    public void call(List<Entry> entries) {
                        entryIndex = 0;
                        sendMsgRefreshLineChart(entries);
                    }
                });
    }


    //刷新常规页面数据
    private void sendMsgShowRealInfo() {
        MsgHelper.sendEvent(GlobalEventT.station_refresh_entry_info, "", currentEntry);
    }

    private void sendMsgRefreshLineChart(List<Entry> entries) {
        MsgHelper.sendEvent(GlobalEventT.station_refresh_chart_entry_record, "", entries);
    }

    private void sendMsgSnackBarTip(String tip, boolean isSuccess) {
        MsgHelper.sendEvent(GlobalEventT.station_bottom_snack_tip, tip, isSuccess);
    }

}
