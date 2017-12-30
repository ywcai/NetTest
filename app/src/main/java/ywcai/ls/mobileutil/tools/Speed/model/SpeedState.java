package ywcai.ls.mobileutil.tools.Speed.model;

import java.util.Calendar;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.results.model.TaskTotal;

public class SpeedState {
    //-1无任务,0有一个任务未开始,1正在任务，2任务完成未保存数据
    public int running = -1;
    public long payloadSize = 0;
    public int useTime = 0;
    public int currentComplete = 0;
    public final int downMaxCount = 2;//9
    public final int readMaxCount = 20;//10
    public long startTime = 0;
    public String speedResult = "";
    public float realSpeed=0.00f;

    //完成下载，仅重置当前的运行状态
    public void complete() {
        //重置为等待处理结果的状态。
        running = 2;
        speedResult = "共下载数据 " + (float) ((int) (payloadSize / 10000) / 100.00) + " MB"
                + " 共耗时: " +  useTime / 1000.00 + " 秒"
                + "\n网络带宽 " + realSpeed + "Mbps";
    }

    public void reset() {
        //重置为等待处理结果的状态。
        running = 0;
        payloadSize = 0;
        useTime = 0;
        currentComplete = 0;
    }

    public void start() {
        //开始任务，将状态置为1，并获取线程启动的时间.
        running = 1;
        payloadSize = 0;
        useTime = 0;
        currentComplete = 0;
        startTime = Calendar.getInstance().getTimeInMillis();
    }


}
