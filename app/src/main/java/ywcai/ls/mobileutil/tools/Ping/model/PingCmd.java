package ywcai.ls.mobileutil.tools.Ping.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.Ping;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.tools.Ping.presenter.inf.PingProcessInf;


/**
 * Created by zmy_11 on 2016/8/17.
 */
public class PingCmd implements Runnable {
    private PingState pingState;
    private PingProcessInf pingProcessInf;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private List<Float> listResult = new ArrayList<Float>();
    private Ping ping = new Ping();

    public PingCmd(PingProcessInf _pingProcessInf, List _list) {
        if (_list != null) {
            listResult.addAll(_list);
        }
        pingProcessInf = _pingProcessInf;
        pingState = cacheProcess.getCachePingState();
        ping.pingLength = pingState.length;
    }

    public void PingTest() {
//        try {
//            Process p = Runtime.getRuntime().exec("ping -c 1 -n -s " + pingState.length + " " + pingState.ipAddress);
//            new BufferedReader(new InputStreamReader(p.getErrorStream()));
//            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            int status = p.waitFor();
//            float y = -40;//LOSS的包在坐标轴中表示的刻度。
//            if (status == 0) {
//                String str = "";
//                while ((str = buf.readLine()) != null) {
//                    if (str.indexOf("icmp_seq=") >= 0 && str.indexOf("time=") >= 0) {
//                        String delay = str.split("=")[3].split(" ")[0];
//                        y = Float.parseFloat(delay);
//                        break;
//                    }
//                }
//            } else {
//                //返回掉包
//                pingState.loss++;
//            }
//            updateState(y);
//        } catch (Exception e) {
//        }

        float result = ping.pingCmd(pingState.ipAddress);
        if (result < 0) {
            pingState.loss++;
        }
        updateState(result);
    }

    @Override
    public void run() {
        PingTest();
    }

    private void updateState(float y) {
        PingState temp = new PingState();
        synchronized (pingState) {
            listResult.add(y);
            pingState.send = listResult.size();
            pingState.nowDelay = y;
            pingState.max = pingState.max <= y ? y : pingState.max;
            pingState.min = (pingState.min <= 0 && y > 0) ? y : (y <= pingState.min && y > 0) ? y : pingState.min;
            pingState.per = (float) (pingState.loss * 10000 / pingState.send) / 100;
            if (y > 0) {
                pingState.average =
                        pingState.send == pingState.loss ? 0 :
                                (float) Math.round((pingState.average * (pingState.send - 1) + y) * 100 / pingState.send) / 100;
            }
            temp.copy(pingState);
            if (temp.send <= temp.packageCount) {
                cacheProcess.setPingResult(AppConfig.INDEX_PING + "-" + pingState.startTime, listResult);
                pingProcessInf.refreshRunningTask(temp);
            }
            if (temp.send == temp.packageCount) {
                pingProcessInf.autoCompleteTask();
            }
        }
    }
}
