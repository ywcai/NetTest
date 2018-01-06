package ywcai.ls.mobileutil.tools.Station.presenter;

import com.github.mikephil.charting.data.Entry;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
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
        stationEntries = cacheProcess.getStationRecord(AppConfig.INDEX_STATION + "-" + stationState.logTime);
    }

    public void refreshInfo(HashMap<String, Integer> cells, HashMap<String, Integer> signals) {
        synchronized (currentEntry) {
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
            }
            if (signals != null) {
                if (signals.get(currentEntry.netTypeName) != null) {
                    currentEntry.rsp = signals.get(currentEntry.netTypeName);
                }
                //保存数据到日志
            }
            saveTempLog();
            sendMsgShowRealInfo();
        }
    }

    private void saveTempLog() {
        StationEntry temp = new StationEntry();
        temp.copy(currentEntry);
        stationEntries.add(0, temp);
        cacheProcess.setStationRecord(AppConfig.INDEX_STATION + "-" + stationState.logTime, currentEntry);
        recoveryChart();
    }


    public void saveLocal() {
        if (stationEntries.size() <= 1) {
            sendMsgSnackBarTip("还没有数据,保存失败", false);
            return;
        }
        Observable.from(stationEntries)
                .filter(new Func1<StationEntry, Boolean>() {
                    @Override
                    public Boolean call(StationEntry stationEntry) {
                        return stationEntry.rsp < -1;
                    }
                }).toSortedList(new Func2<StationEntry, StationEntry, Integer>() {
            @Override
            public Integer call(StationEntry stationEntry, StationEntry stationEntry2) {
                return stationEntry.rsp > stationEntry2.rsp ? 1 : -1;
            }
        }).subscribe(new Action1<List<StationEntry>>() {
            @Override
            public void call(List<StationEntry> list) {
                final LogIndex logIndex = new LogIndex();
                logIndex.logTime = stationState.logTime;
                logIndex.cacheTypeIndex = AppConfig.INDEX_STATION;
                logIndex.aliasFileName = "基站测试日志";
                logIndex.cacheFileName = AppConfig.INDEX_STATION + "-" + stationState.logTime;
                if (list.size() <= 0) {
                    logIndex.remarks = "本次测试并没有记录到任何数据";
                } else {
                    logIndex.remarks = "检测到数据变化 [" + stationEntries.size() + "] 次 " + "最强[" + list.get(list.size() - 1).rsp + "] 最弱[" +
                            list.get(0).rsp + "]";
                }
                logIndex.setAddr();
                resetLog();
            }
        });
        //继续开始一个新的任务
    }

    public void saveRemote() {
        sendMsgSnackBarTip(AppConfig.LOG_REMOTE_SAVE_SUCCESS, false);
    }

    public void clearLog() {
        //删除原有的
        cacheProcess.setStationRecord(AppConfig.INDEX_STATION + "-" + stationState.logTime, null);
        resetLog();
        sendMsgSnackBarTip("您成功重置了本地缓存的数据!", true);
    }

    private void resetLog() {
        stationEntries.clear();
        stationState.logTime = MyTime.getDetailTime();
        cacheProcess.setStationState(stationState);
        sendMsgRefreshLineChart(null);
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
        MsgHelper.sendEvent(GlobalEventT.global_pop_snack_tip, tip, isSuccess);
    }
}
