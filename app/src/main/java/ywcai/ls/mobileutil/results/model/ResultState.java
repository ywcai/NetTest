package ywcai.ls.mobileutil.results.model;

import ywcai.ls.mobileutil.global.cfg.AppConfig;

/**
 * Created by zmy_11 on 2017/11/12.
 */

public class ResultState {
    public boolean isShowLocal = true;
    public int[] isShow = new int[AppConfig.getMenuTextStr().length];
}
