package ywcai.ls.mobileutil.tools.Sensor.presenter;

import android.content.Context;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsListTransfer;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.Sensor.model.SensorProcess;
import ywcai.ls.mobileutil.tools.Sensor.model.SensorState;
import ywcai.ls.mobileutil.tools.Sensor.presenter.inf.SensorActionInf;

public class SensorAction implements SensorActionInf {
    private SensorState sensorState;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private SensorProcess sensorProcess;

    public SensorAction() {
        this.sensorState = cacheProcess.getSensorState();
        sensorProcess = new SensorProcess();
    }

    @Override
    public void checkDeviceSensors(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sensorProcess.checkSensorList(context);
            }
        }).start();
    }

    @Override
    public void recoverySensorSelect() {

    }

    @Override
    public void clickSensorTag(int[] selects) {
        int count = LsListTransfer.count(selects);
        if (count <= 3) {
            sensorState.selects = LsListTransfer.copyInts(selects);
            cacheProcess.setSensorState(sensorState);
            sensorProcess.updateListener(selects);
        } else {
            int[] temp = LsListTransfer.copyInts(sensorState.selects);
            sendMsgRecoverySelectTag(temp);
            sendMsgSnackTip("允许同时监听传感器数量不超过3个！");
        }
    }

    @Override
    public void destroyListener() {
        sensorProcess.unregisterAllSensor();
    }

    private void sendMsgSnackTip(String tip) {
        MsgHelper.sendEvent(GlobalEventT.sensor_set_snack_tip, tip, null);
    }

    private void sendMsgRecoverySelectTag(int[] temp) {
        MsgHelper.sendEvent(GlobalEventT.sensor_recovery_tag_state, "", temp);
    }

}
