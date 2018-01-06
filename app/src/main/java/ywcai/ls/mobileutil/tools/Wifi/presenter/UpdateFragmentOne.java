package ywcai.ls.mobileutil.tools.Wifi.presenter;

import android.support.v4.content.ContextCompat;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.Wifi.model.one.WifiDrawDetail;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.UpdateFragmentOneInf;

public class UpdateFragmentOne implements UpdateFragmentOneInf {
    private WifiState wifiState;

    public UpdateFragmentOne(WifiState wifiState) {
        this.wifiState = wifiState;
    }

    @Override
    public void loadChannelTagStatus() {
        sendMsgRecovery2d4gTag();
        sendMsgRecovery5gTag();
    }


    @Override
    public void loadLockAndSaveVisible() {
        sendMsgLockAndSaveBtnVisible();//根据当前是否有选择实体来设置锁定、解锁按钮的可见性
    }

    @Override
    public void loadLockBtnStatus() {
        sendMsgLockBtnStatus();
    }

    @Override
    public void loadTaskBtnStatus() {
        Observable.from(wifiState.saveWifiList)
                .exists(new Func1<WifiEntry, Boolean>() {
                    @Override
                    public Boolean call(WifiEntry wifiEntry) {
                        if (wifiState.selectEntry != null) {
                            return wifiEntry.bssid.equals(wifiState.selectEntry.bssid);
                        } else {
                            return false;
                        }
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isExist) {
                        sendMsgTaskBtnStatus(isExist);
                    }
                });
    }


    @Override
    public void loadSignalChangeData(List<WifiEntry> allList, final int[] channelSum) {
        final WifiDrawDetail wifiDrawDetail = new WifiDrawDetail();
        Observable.from(allList).
                filter(new Func1<WifiEntry, Boolean>() {
                    @Override
                    public Boolean call(WifiEntry wifiEntry) {

                        return wifiState.choose2d4G == wifiEntry.is2G;
                    }
                })
                .filter(new Func1<WifiEntry, Boolean>() {
                    @Override
                    public Boolean call(WifiEntry wifiEntry) {
                        if (wifiState.choose2d4G) {
                            return wifiState.select2d4G[wifiEntry.channel - 1] == 1;
                        } else {
                            return wifiState.select5G[(wifiEntry.channel - 149) / 4] == 1;
                        }
                    }
                })
                .map(new Func1<WifiEntry, WifiEntry>() {
                    @Override
                    public WifiEntry call(WifiEntry wifiEntry) {
                        //如果当前选择有高亮显示的信号，则找出来，放到draw的信息中去，因为要更新其当前信号信息
                        if (wifiState.selectEntry != null) {
                            if (wifiEntry.bssid.equals(wifiState.selectEntry.bssid)) {
                                wifiDrawDetail.setSelectEntry(wifiEntry);
                            }
                        }
                        //如果wifiState.selectEntry为null，则不高亮显示，则wifiDrawDetail中携带高亮NULL实体.
                        return wifiEntry;
                    }
                })
                .toList()
                .subscribe(new Action1<List<WifiEntry>>() {
                    @Override
                    public void call(List<WifiEntry> wifiEntries) {
                        wifiDrawDetail.wifiList = wifiEntries;
                        //利用当前缓存的高亮对象来更新高亮显示的位置,必须利用原图自动产生的高亮实体，否则信息不全，无法生成。
                        wifiDrawDetail.refreshHighlights(wifiState.highlights);
                        wifiDrawDetail.setDes(channelSum);
                        sendMsgDrawRadarChartAndTagColor(wifiDrawDetail);
                    }
                });
    }

    @Override
    public void addTaskEnd(String tip, boolean success) {
        if (success) {
            sendMsgPopBottomSnack(tip, -1);
            sendMsgNotification(tip);
        } else {
            sendMsgPopBottomSnack(tip, R.color.tipFail);
        }
    }

    @Override
    public void showSelectEntryInfo(WifiEntry wifiEntry) {
        //不为空，可能是直接选择的，也可能是刷新获取到的,获取到的是直接在SCAN中拿到的数据，否则lockEntry为空
        if (wifiEntry != null) {
            sendMsgCurrentSelectWifi(wifiEntry);
        }
        //如果在scan中没有获取到数据
        else {
            //如果当前list中没有信号，则在缓存中有选择到高亮实体，并提示他数据丢失了
            if (wifiState.selectEntry != null) {
                wifiState.selectEntry.dbm = -160;
                //这里保存缓存
                CacheProcess.getInstance().setWifiState(wifiState);
                sendMsgCurrentSelectWifi(wifiState.selectEntry);//显示保存的缓存数据
            } else {
                //还原无选择状态
                sendMsgCurrentSelectWifi(null);
            }
        }
    }

    //
    private void sendMsgPopBottomSnack(String tip, int color) {
        MsgHelper.sendEvent(GlobalEventT.global_pop_snack_tip, tip, color);
    }

    private void sendMsgNotification(String tip) {
        MsgHelper.sendEvent(GlobalEventT.wifi_notify_top_notification, tip, null);
    }


    private void sendMsgRecovery2d4gTag() {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_recovery_channel_select_2d4g, "", wifiState.select2d4G);
    }

    private void sendMsgRecovery5gTag() {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_recovery_channel_select_5g, "", wifiState.select5G);
    }


    private void sendMsgCurrentSelectWifi(WifiEntry wifiEntry) {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_set_select_entry_info, "", wifiEntry);
    }


    private void sendMsgDrawRadarChartAndTagColor(WifiDrawDetail wifiDrawDetail) {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_refresh_first_info, "", wifiDrawDetail);
    }

    private void sendMsgTaskBtnStatus(Boolean isExist) {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_set_task_btn_status, "", isExist);
    }


    private void sendMsgLockBtnStatus() {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_set_lock_btn_status, "", wifiState.lockWifi);
    }

    private void sendMsgLockAndSaveBtnVisible() {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_set_lock_save_btn_visible, "", wifiState.selectEntry != null);
    }
}
