package ywcai.ls.mobileutil.tools.Wifi.presenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.UpdateFragmentTwoInf;

/**
 * Created by zmy_11 on 2017/10/21.
 */

public class UpdateFragmentTwo implements UpdateFragmentTwoInf {
    WifiState wifiState;
    CacheProcess cacheProcess = CacheProcess.getInstance();

    public UpdateFragmentTwo(WifiState _wifiState) {
        wifiState = _wifiState;
    }

    @Override
    public void refreshList(final List<WifiEntry> allList) {
        Observable.from(wifiState.saveWifiList)
                .map(new Func1<WifiEntry, WifiEntry>() {
                    @Override
                    public WifiEntry call(WifiEntry wifiEntry) {
                        for (int i = 0; i < allList.size(); i++) {
                            if (wifiEntry.bssid.equals(allList.get(i).bssid)) {
                                wifiEntry.dbm = allList.get(i).dbm;
                                saveTaskData(wifiEntry);
                                return wifiEntry;
                            }
                        }
                        wifiEntry.dbm = -160;
                        saveTaskData(wifiEntry);
                        return wifiEntry;
                    }
                })
                .toList()
                .subscribe(new Action1<List<WifiEntry>>() {
                    @Override
                    public void call(List<WifiEntry> wifiEntries) {
                        sendMsgUpdateTaskList(wifiEntries);
                    }
                });
    }

    @Override
    public void refreshChart() {
        sendMsgRefreshChart();
    }

    @Override
    public void popOperatorMenu() {
        sendMsgPopMenu();
    }

    @Override
    public void updateItemBtn(int pos, boolean isShowInChart) {
        sendMsgUpdateItemBtn(pos, isShowInChart);
    }

    private void saveTaskData(WifiEntry wifiEntry) {
        cacheProcess.setWifiTaskResult(wifiEntry);
    }





    private void sendMsgUpdateTaskList(List<WifiEntry> wifiEntryList) {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_refresh_two_list, "", wifiEntryList);
    }

    private void sendMsgPopMenu() {
        MsgHelper.sendEvent(GlobalEventT.wifi_pop_menu, "", null);
    }

    private void sendMsgRefreshChart() {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_refresh_two_chart_line, "", null);
    }

    private void sendMsgUpdateItemBtn(int pos, boolean isShowInChart) {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_set_item_btn_hide_status, pos + "", isShowInChart);

    }

}
