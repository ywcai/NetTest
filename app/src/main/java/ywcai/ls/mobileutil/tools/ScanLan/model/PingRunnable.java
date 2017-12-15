package ywcai.ls.mobileutil.tools.ScanLan.model;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.Ping;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;

public class PingRunnable implements Runnable {
    int index;
    ScanLanState scanState;

    public void setIndex(int index) {

        this.index = index;
    }

    public void setScanState(ScanLanState scanState) {
        this.scanState = scanState;
    }

    @Override
    public void run() {
        float result = -40;
        //如果获取到本地IP是0.0.0.0，则直接返回失败
        Ping ping = new Ping();
        result = ping.pingCmd(scanState.maskNet + index);
        //如果失败，则再PING一次.
        if (result < 0 && (!this.scanState.maskNet.equals("0.0.0."))) {
            result = ping.pingCmd(this.scanState.maskNet + index);
        }
        synchronized (scanState) {
            scanState.scanCount++;
            ScanLanResult scanLanResult = new ScanLanResult();
            scanLanResult.count = scanState.scanCount;
            scanLanResult.index = index;
            scanLanResult.max = scanState.maxCount;
            scanLanResult.pingResult = result;
            MsgHelper.sendEvent(GlobalEventT.scan_lan_ping_index_result, "", scanLanResult);
            if (scanState.scanCount >= scanState.maxCount) {
                MsgHelper.sendEvent(GlobalEventT.scan_lan_ping_stop, "", null);
            }
        }
    }
}
