package ywcai.ls.mobileutil.tools.ScanLan.model;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.ConvertUtil;

public class LocalNetInfo {


    //如果连接WIFI，则直接获取WIFI分配的地址
    public String getLocalIp() {
        String localIp = "0.0.0.0";
        try {
            WifiManager wifiMg = (WifiManager) MainApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            android.net.wifi.WifiInfo wifiInfo = wifiMg.getConnectionInfo();
            localIp = ConvertUtil.ConvertIpToStr(wifiInfo.getIpAddress());
        } catch (Exception e) {
        }
        //如果没有连接，则从本地缓存获取地址信息。
        if (localIp.equals("0.0.0.0")) {
            localIp = getLocalIpForLinuxLog();
        }
        //如果本地也没有开热点，则返回0.0.0.0，有具体业务调用层再去处理。
        return localIp;
    }

    //获取本地WIFI模块的MAC地址
    public String getLocalMac() {
        String mac = "null";
        File arpFile = new File(AppConfig.STR_LOCAL_MAC_FILE);
        InputStream inStream;
        try {
            inStream = new FileInputStream(arpFile);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inStreamReader);
            mac = buffReader.readLine();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }

    //如果本机是热点，返回本机获取到的IP值（一般本机IP地址为1）,热点IP一般为246
    private String getLocalIpForLinuxLog() {
        String localIp = "0.0.0.0";
        File arpFile = new File(AppConfig.STR_ARP_FILE_PATH);
        InputStream inStream;
        try {
            inStream = new FileInputStream(arpFile);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inStreamReader);
            String temp;
            buffReader.readLine();
            if ((temp = buffReader.readLine()) != null) {
                temp = temp.replaceAll("\\s+", " ");
                String[] result = temp.split(" ");
                localIp = result[0].substring(0, result[0].lastIndexOf(".")) + ".1";
            }
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localIp;
    }
}
