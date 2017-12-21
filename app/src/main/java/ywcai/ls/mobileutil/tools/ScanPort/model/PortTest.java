package ywcai.ls.mobileutil.tools.ScanPort.model;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;

import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.results.model.TaskTotal;

/**
 * Created by zmy_11 on 2017/12/17.
 */

public class PortTest implements Runnable {

    private ScanPortState scanPortState;
    private ScanPortResult scanPortResult;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private int testPort;


    public void setConfig(ScanPortState scanPortState, ScanPortResult scanPortResult) {
        this.scanPortResult = scanPortResult;
        this.scanPortState = scanPortState;
    }

    public void setTestPort(int testPort) {
        this.testPort = testPort;
    }

    @Override
    public void run() {
        boolean isOpen;
        Socket socket;
        try {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(scanPortState.targetIp, testPort);
            socket.connect(socketAddress, 5000);
        } catch (Exception e) {
            socket = null;
        }
        if (socket == null) {
            isOpen = false;
        } else {
            if (!socket.isConnected()) {
                isOpen = false;
            } else {
                isOpen = true;
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (scanPortState.scanTaskState != 1) {
            return;
        }
        processResult(isOpen);
    }

    private void processResult(boolean b) {
        synchronized (scanPortResult) {
            scanPortResult.currentScanIndex++;
            ScanPortResult temp = new ScanPortResult();
            temp.currentScanIndex = scanPortResult.currentScanIndex;
            temp.maxCounts = scanPortResult.maxCounts;
            if (b) {
                scanPortResult.openPorts.add(testPort);
                temp.openPorts.addAll(scanPortResult.openPorts);
                sendMsgAddResult(temp);
            } else {
                //
                temp.openPorts.addAll(scanPortResult.openPorts);
            }
            cacheProcess.setScanPortResult(scanPortResult);
            TaskTotal taskTotal = cacheProcess.getCacheTaskTotal();
            taskTotal.state[AppConfig.INDEX_PORT] = temp.currentScanIndex * 100 / temp.maxCounts;
            taskTotal.autoCount();
            cacheProcess.setCacheTaskTotal(taskTotal);
            sendMsgRefreshProgress(temp);
            if (scanPortResult.currentScanIndex >= scanPortResult.maxCounts - 1) {
                scanPortState.scanTaskState = 2;
                cacheProcess.setScanPortState(scanPortState);
                sendMsgScanAutoEnd();
            }
        }
    }

    private void sendMsgRefreshProgress(ScanPortResult temp) {
        MsgHelper.sendStickEvent(GlobalEventT.scan_port_refresh_radar_progress, "", temp);
    }

    private void sendMsgAddResult(ScanPortResult temp) {
        MsgHelper.sendStickEvent(GlobalEventT.scan_port_add_radar_result, "", temp);
    }

    private void sendMsgScanAutoEnd() {
        MsgHelper.sendStickEvent(GlobalEventT.scan_port_set_card_run_info_end, "", scanPortState);
    }
}
