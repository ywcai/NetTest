package ywcai.ls.mobileutil.http.model;

import java.util.List;

import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.tools.Ping.model.PingState;

/**
 * Created by zmy_11 on 2017/10/3.
 */

public class HttpPing {
    CacheProcess cacheProcess=CacheProcess.getInstance();
    public boolean execute()
    {
        PingState state=cacheProcess.getCachePingState();
        List list=cacheProcess.getPingResult("PING"+state.startTime);
        if(!requestState(state))
        {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
     return  requestData(list);
    }
    private boolean requestState(PingState state)
    {

        return true;
    }
    private boolean requestData(List<Float> list)
    {

        return true;
    }

}
