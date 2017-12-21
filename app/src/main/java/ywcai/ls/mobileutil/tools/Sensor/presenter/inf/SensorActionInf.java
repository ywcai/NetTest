package ywcai.ls.mobileutil.tools.Sensor.presenter.inf;

import android.content.Context;

/**
 * Created by zmy_11 on 2017/12/20.
 */

public interface SensorActionInf {
    void checkDeviceSensors(Context context);

    void recoverySensorSelect();

    void clickSensorTag(int[] ints);
}
