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
}
