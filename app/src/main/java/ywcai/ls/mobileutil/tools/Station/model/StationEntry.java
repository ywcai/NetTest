package ywcai.ls.mobileutil.tools.Station.model;

import android.content.Context;
import android.telephony.TelephonyManager;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;

/**
 * Created by zmy_11 on 2017/10/29.
 */

public class StationEntry {
    public int lac = -1, cid = -1, rsp = -1, netType = -1;
    public String mobileNum = "null", driveId = "null", simSerialNumber = "null";
    public String networkOperator = "null", networkOperatorName = "null";
    private String net = "null";

    public String getNet() {
        return convertNet(netType);
    }

    @Override
    public String toString() {
        return "StationEntry{" +
                "lac=" + lac +
                ", cid=" + cid +
                ", rsp=" + rsp +
                ", netType=" + netType +
                ", mobileNum='" + mobileNum + '\'' +
                ", driveId='" + driveId + '\'' +
                ", simSerialNumber='" + simSerialNumber + '\'' +
                ", networkOperator='" + networkOperator + '\'' +
                ", networkOperatorName='" + networkOperatorName + '\'' +
                ", net='" + getNet() + '\'' +
                '}';
    }

    public void setBaseData() {
        TelephonyManager telephonyManager = (TelephonyManager) MainApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        netType = telephonyManager.getNetworkType();
        try {
            driveId=telephonyManager.getDeviceId();
            simSerialNumber = telephonyManager.getSimSerialNumber().equals("") ? "null" : telephonyManager.getSimSerialNumber();
            mobileNum = telephonyManager.getLine1Number().equals("") ? "null" : telephonyManager.getLine1Number();
            networkOperator = telephonyManager.getNetworkOperator().equals("") ? "null" : telephonyManager.getNetworkOperator();
            networkOperatorName = telephonyManager.getNetworkOperatorName().equals("") ? "null" : telephonyManager.getNetworkOperatorName();
        } catch (Exception e) {
            MsgHelper.sendEvent(GlobalEventT.station_set_entry_change, e.getMessage(), null);
        }
    }

    private String convertNet(int netType) {
        String temp = "";
        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                temp = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                temp = "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                temp = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                temp = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                temp = "EHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                temp = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                temp = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                temp = "EVDO_B";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                temp = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                temp = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                temp = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                temp = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                temp = "UMTS";
                break;
            default:
                temp = "UNKNOWN";
                break;
        }
        return temp;
    }

}
