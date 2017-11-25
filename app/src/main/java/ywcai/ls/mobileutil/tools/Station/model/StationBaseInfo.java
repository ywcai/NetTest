package ywcai.ls.mobileutil.tools.Station.model;

import android.content.Context;
import android.telephony.TelephonyManager;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;

/**
 * Created by zmy_11 on 2017/10/29.
 */

public class StationBaseInfo {
    public boolean isOldOS, isDoubleCard;
    public String telNum = "null", driveId = "null", simSerialNumber = "null";
    public String networkOperator = "null", networkOperatorName = "null";
    public int netType;
    public String netTypeName;

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
