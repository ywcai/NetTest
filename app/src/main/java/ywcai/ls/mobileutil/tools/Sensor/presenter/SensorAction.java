package ywcai.ls.mobileutil.tools.Sensor.presenter;

import android.content.Context;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsListTransfer;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
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
    public void checkDeviceSensors(Context context) {

    }

    @Override
    public void recoverySensorSelect() {
        //
    }

    @Override
    public void clickSensorTag(int[] tags) {
        int count = LsListTransfer.count(tags);
        LsLog.saveLog(count + "");
        if (count > 3) {
            //如果大于3个，恢复上一次选择的状态
//                    sendMsgRecoverySelectTag();
            sendMsgSnackTip("同时查看的传感器不能超过3个！");

            //提示超过了3个的错误
        } else {
            //do some thing;
            sensorState.tags = tags;
            cacheProcess.setSensorState(sensorState);
        }
    }


    private void sendMsgSnackTip(String tip) {
        MsgHelper.sendEvent(GlobalEventT.sensor_set_snack_tip, tip, null);
    }

    private void sendMsgRecoverySelectTag() {
        MsgHelper.sendEvent(GlobalEventT.sensor_recovery_tag_state, "", sensorState.tags);
    }


}
