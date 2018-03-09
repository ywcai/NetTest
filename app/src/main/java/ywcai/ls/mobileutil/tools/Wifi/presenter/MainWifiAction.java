package ywcai.ls.mobileutil.tools.Wifi.presenter;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.highlight.Highlight;
import rx.functions.Action1;
import ywcai.ls.mobileutil.global.util.statics.InStallService;
import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.WifiService;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;

public class MainWifiAction implements MainWifiActionInf {
    WifiService wifiService = null;
    LsConnection lsConnection;
    WifiProcess wifiProcess;

    public MainWifiAction() {
        wifiProcess = new WifiProcess();
        startWifiService();
    }

    @Override
    public void startWifiService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bindService();
                waitService();
            }
        }).start();
    }

    private void bindService() {
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
        InStallService.bindService(WifiService.class, lsConnection);
    }

    private void waitService() {
        InStallService.waitService(wifiService);
        //绑定后启动扫描，如果已经存在扫描的线程，则在子方法中判断
        wifiProcess.setWifiService(wifiService);
    }

    @Override
    public void lockWifi() {
        wifiProcess.lock();
    }

    @Override
    public void setSelectEntry(WifiEntry wifiEntry) {

        wifiProcess.setSelectEntry(wifiEntry);
    }

    @Override
    public void saveHighLightHolderView(Highlight[] hh) {

        wifiProcess.saveHighlightHolder(hh);
    }


    @Override
    public void addOrRemoveTask() {

        wifiProcess.task();
    }


    @Override
    public void set2d4G(boolean choose2d4G) {

        wifiProcess.set2d4G(choose2d4G);
    }

    @Override
    public void setChannelFilter(int index, boolean isSelect) {
        wifiProcess.setChannelFilter(index, isSelect);
    }

    @Override
    public void setAllTagSelectOrCancal(int[] allTagStatus) {

        wifiProcess.setAllTagSelectOrCancal(allTagStatus);
    }


    @Override
    public void saveLogForLocal(int pos) {

        wifiProcess.saveLogForLocal(pos);

    }

    @Override
    public void saveLogForRemote(int pos) {

        wifiProcess.saveLogForRemote(pos);
    }

    @Override
    public void clearLog(int pos) {

        wifiProcess.clearLog(pos);
    }

    @Override
    public void showChartLine(int popTaskPos) {

        wifiProcess.setLineHide(popTaskPos);
    }

    @Override
    public void saveBitmap(LineChart wifiChannelRecord) {

        wifiProcess.saveBitMap(wifiChannelRecord);
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

        wifiProcess.popOperatorMenu();
    }
}
