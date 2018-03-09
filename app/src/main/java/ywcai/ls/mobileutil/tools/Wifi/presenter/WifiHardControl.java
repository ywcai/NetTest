package ywcai.ls.mobileutil.tools.Wifi.presenter;

import android.content.Context;
import android.net.wifi.WifiManager;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;


public class WifiHardControl {
    private Context context = MainApplication.getInstance().getApplicationContext();
    private WifiManager wifiMg = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    public int checkScanCount = 0, selfAdd = 0, scanAutoFlag = 0;

    public void startWifiScan() {
        long timeout = AppConfig.INT_WIFI_AUTO_SCAN_REFRESH;
        //当系统自动扫描的标记大于等于设定的最大值，表明系统是自动刷新的，不需要在手动调度刷新扫描
        while (scanAutoFlag < AppConfig.INT_CHECK_WIFI_AUTO_SCAN_COUNT) {
            //如果线程自检的次数已经少于WIFI搜索的次数，说明已经搜索到信号，增加一次自动刷新的标志.
            if (checkScanCount < selfAdd) {
                scanAutoFlag++;
            } else {
                wifiMg.startScan();
            }
            checkScanCount = 0;
            selfAdd = 0;
            //扫描次数如果大于或等于接收广播次数，说明循环周期内手机没有自动搜索WIFI信号，恢复原始值，手动启动一次扫描；
            //第一次启动始终会进行扫描

            //第一次休眠5秒，隋饶如果已经有自动增加的标记，则同步增加休眠周期
            try {
                Thread.sleep(timeout + scanAutoFlag * 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //扫描次数+1；
            checkScanCount++;
        }
    }
}