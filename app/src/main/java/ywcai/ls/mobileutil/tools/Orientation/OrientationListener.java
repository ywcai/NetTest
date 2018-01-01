package ywcai.ls.mobileutil.tools.Orientation;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;

public class OrientationListener implements SensorEventListener {

    @Override
    public void onSensorChanged(SensorEvent event) {
        float rate = ((int) (event.values[0] * 100)) / 100.00f;
        sendMsgRefreshRate(rate);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void sendMsgRefreshRate(float rate) {
        MsgHelper.sendStickEvent(GlobalEventT.orientation_refresh, "", rate);
    }
}
