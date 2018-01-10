package ywcai.ls.mobileutil.tools.Station.model;

import android.telephony.TelephonyManager;

import ywcai.ls.mobileutil.global.util.statics.MyTime;

public class StationEntry {
    public int cid = 0, lac = 0, rsp = 0, netType = -1;
    public String networkOperator = "null", networkOperatorName = "null";
    //用netTypeName对应发射获取的场强字段
    public String netTypeName = "UNKNOWN", netTypeCn = "UNKNOWN";
    public final String UNKNOWN = "UNKNOWN";
    public String imei = "", cardNumber = "";
    public String currentTime = MyTime.getDetailTime();

    public void setNetTypeName() {
        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                netTypeName = "mGsmSignalStrength";
                netTypeCn = "GSM";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                netTypeName = "mGsmSignalStrength";
                netTypeCn = "GSM";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                netTypeName = "mCdmaDbm";
                netTypeCn = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                netTypeName = "mGsmSignalStrength";
                netTypeCn = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                netTypeName = "mCdmaDbm";
                netTypeCn = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                netTypeName = "mCdmaDbm";
                netTypeCn = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                netTypeName = "mCdmaDbm";
                netTypeCn = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                netTypeName = "mCdmaDbm";
                netTypeCn = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                netTypeName = "mWcdmaRscp";
                netTypeCn = "WCDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                netTypeName = "mWcdmaRscp";
                netTypeCn = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                netTypeName = "mWcdmaRscp";
                netTypeCn = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                netTypeName = "mWcdmaRscp";
                netTypeCn = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                netTypeName = "mLteRsrp";
                netTypeCn = "Lte";
                break;
            default:
                netTypeName = "UNKNOWN";
                netTypeCn = "UNKNOWN";
                break;
        }
    }


    public void copy(StationEntry currentEntry) {
        cid = currentEntry.cid;
        lac = currentEntry.lac;
        rsp = currentEntry.rsp;
        netType = currentEntry.netType;
        networkOperator = currentEntry.networkOperator;
        networkOperatorName = currentEntry.networkOperatorName;
        netTypeName = currentEntry.netTypeName;
        netTypeCn = currentEntry.netTypeCn;
        imei = currentEntry.imei;
        cardNumber = currentEntry.cardNumber;
        currentTime = MyTime.getDetailTime();
    }

    @Override
    public String toString() {
        return "[" + currentTime + "]\n" +
                "小区号=" + cid +
                ", 区域码=" + lac +
                ", 场强=" + rsp +
                ", 网络类别=" + netTypeCn;

    }
}
