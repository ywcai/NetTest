package ywcai.ls.mobileutil.tools.Wifi.presenter;


import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.highlight.Highlight;

import rx.functions.Action1;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.InStallService;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.util.statics.MyTime;
import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.StationService;
import ywcai.ls.mobileutil.service.WifiService;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;

public class MainWifiAction implements MainWifiActionInf {
    WifiService wifiService = null;
    LsConnection lsConnection;
    Context context;

    public MainWifiAction(Context context) {
        this.context=context;
        lsConnection = new LsConnection(new Action1() {
            @Override
            public void call(Object o) {
                if (o != null) {
                    wifiService = (WifiService) o;
                } else {
                    wifiService = null;
                }
            }
        });
        InStallService.bindService(context, WifiService.class, lsConnection);
    }

    @Override
    public void startWifiService() {
        InStallService.waitService(wifiService);//绑定服务是异步的，必须等待，否则可能启动时空指针
        if (wifiService != null) {
            wifiService.installProcess();
//            wifiService.wifiControl.startWifiScan();
        }
    }

    @Override
    public void lockWifi() {
        if (wifiService != null) {
            wifiService.wifiDataProcess.lock();
        }
//        wifiDataProcess.lock();
    }

    @Override
    public void setSelectEntry(WifiEntry wifiEntry) {
        if (wifiService != null) {
            wifiService.wifiDataProcess.setSelectEntry(wifiEntry);
        }
//        wifiDataProcess.setSelectEntry(wifiEntry);
    }

    @Override
    public void saveHighLightHolderView(Highlight[] hh) {
        if (wifiService != null) {
            wifiService.wifiDataProcess.saveHighlightHolder(hh);
        }
//        wifiDataProcess.saveHighlightHolder(hh);
    }


    @Override
    public void addOrRemoveTask() {
        if (wifiService != null) {
            wifiService.wifiDataProcess.task();
        }
//        wifiDataProcess.task();
    }


    @Override
    public void set2d4G(boolean choose2d4G) {
        if (wifiService != null) {
            wifiService.wifiDataProcess.set2d4G(choose2d4G);
        }
//        wifiDataProcess.set2d4G(choose2d4G);
    }

    @Override
    public void setChannelFilter(int index, boolean isSelect) {
        if (wifiService != null) {
            wifiService.wifiDataProcess.setChannelFilter(index, isSelect);
        }
//        wifiDataProcess.setChannelFilter(index);
    }

    @Override
    public void setAllTagSelectOrCancal(int[] allTagStatus) {
        if (wifiService != null) {
            wifiService.wifiDataProcess.setAllTagSelectOrCancal(allTagStatus);
        }
    }


    @Override
    public void saveLogForLocal(int pos) {
        if (wifiService != null) {
            wifiService.wifiDataProcess.saveLogForLocal(pos);
        }
//        wifiDataProcess.saveLogIndexOnLocal(pos);
//        wifiDataProcess.removeTask(pos);
    }

    @Override
    public void saveLogForRemote(int pos) {

    }

    @Override
    public void clearLog(int pos) {
        if (wifiService != null) {
            wifiService.wifiDataProcess.clearLog(pos);
        }
    }

    @Override
    public void showChartLine(int popTaskPos) {
        if (wifiService != null) {
            wifiService.wifiDataProcess.setLineHide(popTaskPos);
        }
//        wifiDataProcess.setLineHide(popTaskPos);
    }

    @Override
    public void saveBitmap(LineChart wifiChannelRecord) {

        if (wifiService != null) {
            wifiService.wifiDataProcess.saveBitMap(wifiChannelRecord);
        }
    }


    @Override
    public void saveAllLogForLocal() {

    }

    @Override
    public void saveAllLogForRemote() {

    }

    @Override
    public void clearAllLog() {

    }

    @Override
    public void clickTaskItem() {
        if (wifiService != null) {
            wifiService.wifiDataProcess.popOperatorMenu();
        }
    }


}
