package ywcai.ls.mobileutil.tools.Wifi.model;

import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;

/**
 * Created by zmy_11 on 2017/10/21.
 */

public class WifiState {
    public boolean choose2d4G = true;
    public int[] select2d4G = new int[13];
    public int[] select5G = new int[5];
    public boolean lockWifi = false;
    public List<WifiEntry> saveWifiList = new ArrayList<>();
    public Highlight[] highlights = null;
    public WifiEntry selectEntry;


    public WifiState() {
        select2d4G[0] = 1;
        select5G[0] = 1;
    }

    public void setHighlights(Highlight[] hh) {
        highlights = hh;
    }

    public void setChannelFilter(int index, boolean isSelect) {
        if (choose2d4G) {
            select2d4G[index] = isSelect ? 1 : 0;
        } else {
            select5G[index] = isSelect ? 1 : 0;
        }
    }
    public void setAllChannelStatus(int[] index) {
        if (choose2d4G) {
            select2d4G = index;
        } else {
            select5G = index;
        }
    }
}
