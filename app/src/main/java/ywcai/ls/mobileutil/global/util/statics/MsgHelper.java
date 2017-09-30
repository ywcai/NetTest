package ywcai.ls.mobileutil.global.util.statics;

import org.greenrobot.eventbus.EventBus;

import ywcai.ls.mobileutil.global.model.GlobalEvent;

/**
 * Created by zmy_11 on 2017/7/15.
 */

public class MsgHelper {

    public static void sendEvent(int type,String tip,Object object)
    {
        GlobalEvent event=new GlobalEvent();
        event.type=type;
        event.tip=tip;
        event.obj=object;
        EventBus.getDefault().post(event);
    }
}
