package ywcai.ls.mobileutil.tools.Ping.model;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.global.util.statics.MyTime;

/**
 * Created by zmy_11 on 2016/8/17.
 */
public class PingState {
    public String ipAddress = "";
    public boolean isRunning = false;
    public boolean isProcessResult = true;
    public boolean isPause=false;
    public boolean isAutoRecovery=false;
    public int send = 1;
    public int loss = 0;
    public float per = 0.0f;
    public float max = 0.0f;
    public float min = 0.0f;
    public float average = 0;
    public int length = 100;
    public int packageCount = 500;
    public int threadCount = 20;
    public String startTime = MyTime.getDetailTime();
    public float nowDelay = 0.0f;


    public void setNewRunningState(String ip) {
        send = 0;
        loss = 0;
        per = 0.00f;
        max = 0f;
        min = 0f;
        average = 0f;
        ipAddress=ip;
        startTime = MyTime.getDetailTime();
        isRunning = true;
        isProcessResult=false;
        isPause=false;
        isAutoRecovery=false;
    }
    public void setProcessEndState() {
        send = 0;
        loss = 0;
        per = 0.00f;
        max = 0f;
        min = 0f;
        average = 0f;
        startTime = MyTime.getDetailTime();
        isRunning = false;
        isProcessResult=true;
        isPause=false;
        isAutoRecovery=false;
    }
    public void setStopState() {
        isRunning = false;
        isProcessResult=false;
        isPause=false;
        isAutoRecovery=false;
    }
    public void setPauseState() {
        isRunning = true;
        isProcessResult=false;
        isPause=true;
        isAutoRecovery=false;
    }
    public void setResumeState() {
        isRunning = true;
        isProcessResult=false;
        isPause=false;
        isAutoRecovery=false;
    }

    public void copy(PingState pingState) {
        ipAddress = pingState.ipAddress;
        isRunning = pingState.isRunning;
        isProcessResult = pingState.isProcessResult;
        send = pingState.send;
        loss = pingState.loss;
        per = pingState.per;
        max = pingState.max;
        min = pingState.min;
        average = pingState.average;
        length = pingState.length;
        packageCount = pingState.packageCount;
        threadCount = pingState.threadCount;
        nowDelay=pingState.nowDelay;
        startTime=pingState.startTime;
        isPause=pingState.isPause;
        isAutoRecovery=pingState.isAutoRecovery;
    }
}
