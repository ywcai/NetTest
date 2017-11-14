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
    public List<Integer> select2d4GList = new ArrayList<>();
    public List<Integer> select5GList = new ArrayList<>();
    public boolean lockWifi = false;
    public List<WifiEntry> saveWifiList = new ArrayList<>();
    public Highlight[] highlights = null;
    public WifiEntry selectEntry;

    public WifiState() {
        select2d4GList.add(AppConfig.INTS_CHANNEL_2D4G[0]);
        select5GList.add(AppConfig.INTS_CHANNEL_5G[0]);
    }

    public void setHighlights(Highlight[] hh) {
        highlights = hh;
    }

    public void setChannelFilter(final int index) {
        if (choose2d4G && (index == 13)) {
            //2.4G全选按钮
            //如果select大小=13,说明已经全选
            if (select2d4GList.size() >= 13) {
                select2d4GList.clear();
            } else {
                select2d4GList.clear();
                for (int i = 0; i < 13; i++) {
                    select2d4GList.add(AppConfig.INTS_CHANNEL_2D4G[i]);
                }
            }
            //恢复按钮.
            return;
        }
        if (!choose2d4G && (index == 5)) {
            //5G全选按钮
            if (select5GList.size() >= 5) {
                select5GList.clear();
            } else {
                select5GList.clear();
                for (int i = 0; i < 5; i++) {
                    select5GList.add(AppConfig.INTS_CHANNEL_5G[i]);
                }
            }
            return;
        }

        if (choose2d4G) {
            Observable.from(select2d4GList).exists(new Func1<Integer, Boolean>() {
                @Override
                public Boolean call(Integer integer) {
                    return integer == AppConfig.INTS_CHANNEL_2D4G[index];
                }
            }).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (aBoolean) {
                        select2d4GList.remove((Object) AppConfig.INTS_CHANNEL_2D4G[index]);
                    } else {
                        select2d4GList.add(AppConfig.INTS_CHANNEL_2D4G[index]);
                    }
//                    updateTagBtnStatus(index, !aBoolean);
                }
            });
        } else {
            Observable.from(select5GList).exists(new Func1<Integer, Boolean>() {
                @Override
                public Boolean call(Integer integer) {

                    return integer == AppConfig.INTS_CHANNEL_5G[index];
                }
            }).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (aBoolean) {
                        select5GList.remove((Object) AppConfig.INTS_CHANNEL_5G[index]);
                    } else {
                        select5GList.add(AppConfig.INTS_CHANNEL_5G[index]);
                    }
//                    updateTagBtnStatus(index, !aBoolean);
                }
            });
        }
    }



}
