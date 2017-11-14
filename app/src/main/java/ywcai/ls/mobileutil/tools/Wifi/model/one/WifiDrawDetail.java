package ywcai.ls.mobileutil.tools.Wifi.model.one;

import com.github.mikephil.charting.highlight.Highlight;

import java.lang.reflect.Field;
import java.util.List;


import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;

/**
 * Created by zmy_11 on 2017/10/18.
 */

public class WifiDrawDetail {
    public List<WifiEntry> wifiList;
    public Highlight[] highlight = null;
    public WifiEntry selectEntry;
    public int[] channelNum;

    //由WifiState传递来的原图高亮实体,只要第一次产生后，就永久缓存最新的。
    public void refreshHighlights(Highlight[] temp) {
        if (selectEntry == null || temp == null || wifiList == null) {
            highlight = null;
            return;
        }
        int x = wifiList.indexOf(selectEntry);
        if (x < 0) {
            highlight = null;
            return;
        }
        //反射
        Class<? extends Highlight> clazz = temp[0].getClass();
        Field mX = null;
        try {
            mX = clazz.getDeclaredField("mX");
            mX.setAccessible(true);
            mX.set(temp[0], x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        temp[0].setDataIndex(x);
        this.highlight = temp;
    }

    public void setDes(int[] channel) {
        channelNum = channel;
    }

    public void setSelectEntry(WifiEntry selectEntry) {
        this.selectEntry = selectEntry;
    }
}
