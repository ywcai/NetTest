package ywcai.ls.mobileutil.tools.Station.presenter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationProcessInf;

/**
 * Created by zmy_11 on 2017/10/30.
 */

public class StationProcess implements StationProcessInf {
    private Context context=MainApplication.getInstance().getApplicationContext();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private  void getSimCard()
    {
        String[] strSimCard=new String[2];
        SubscriptionManager mSubscriptionManager = SubscriptionManager.from(context);
        String tip="";
        tip=mSubscriptionManager.getActiveSubscriptionInfoCount()+"|"+mSubscriptionManager.getActiveSubscriptionInfoCountMax()+
                "|"+mSubscriptionManager.getActiveSubscriptionInfoList().size();
        MsgHelper.sendEvent(GlobalEventT.station_set_entry_change,tip, null);
    }
    private String getSimCard1()
    {
        return "";
    }
    private String getSimCard2()
    {
        return "";
    }


    @Override
    public void addDoubleSimCardListener() {

    }

    @Override
    public void addOnlySimCardListener() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StationListener stationListener=new StationListener();
        try {
            telephonyManager.listen(stationListener, PhoneStateListener.LISTEN_CELL_LOCATION);
            telephonyManager.listen(stationListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
        catch (Exception e)
        {
        }
    }
}
