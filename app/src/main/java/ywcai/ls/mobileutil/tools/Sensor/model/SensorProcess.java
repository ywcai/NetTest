package ywcai.ls.mobileutil.tools.Sensor.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.LsListTransfer;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;


public class SensorProcess implements SensorEventListener {
    List<SensorInfo> list = new ArrayList<>();
    private Context context;
    private HashMap<String, Integer> hashMap = new HashMap<>();

    public void unregisterAllSensor() {
        for (int i = 0; i < list.size(); i++) {
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(list.get(i).type));
        }
    }

    public void updateListener(int[] ints) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Integer index = 1;
        hashMap.clear();
        sendMsgCardTip("", 1);
        sendMsgCardTip("", 2);
        sendMsgCardTip("", 3);
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == 1) {
                sensorManager.registerListener(this, sensorManager.getDefaultSensor(list.get(i).type), SensorManager.SENSOR_DELAY_UI);
                hashMap.put(list.get(i).englishName, index);
                sendMsgCardTip(list.get(i).englishName + "\n" + list.get(i).chineseName, index);
                index++;
            } else {
                sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(list.get(i).type));
            }
        }
    }

    public void checkSensorList(Context context) {
        this.context = context;
        list.clear();
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        final HashMap hashMap2 = new HashMap();
        Observable.from(sensors)
                .filter(new Func1<Sensor, Boolean>() {
                    @Override
                    public Boolean call(Sensor sensor) {
                        if (hashMap2.get(sensor.getType()) != null) {
                            return false;
                        }
                        hashMap2.put(sensor.getType(), true);
                        return true;
                    }
                })
                .map(new Func1<Sensor, SensorInfo>() {
                    @Override
                    public SensorInfo call(Sensor sensor) {
                        SensorInfo sensorInfo = new SensorInfo();
                        sensorInfo.englishName = sensor.getName();
                        sensorInfo.type = sensor.getType();
                        switch (sensor.getType()) {
                            case android.hardware.Sensor.TYPE_GRAVITY:
                                sensorInfo.chineseName = "重力";
                                break;
                            case android.hardware.Sensor.TYPE_ACCELEROMETER:
                                sensorInfo.chineseName = "加速度+重力";
                                break;
                            case android.hardware.Sensor.TYPE_LINEAR_ACCELERATION:
                                sensorInfo.chineseName = "加速度";
                                break;
                            case android.hardware.Sensor.TYPE_GAME_ROTATION_VECTOR:
                                sensorInfo.chineseName = "游戏动作";
                                break;
                            case android.hardware.Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                                sensorInfo.chineseName = "地磁矢量";
                                break;
                            case android.hardware.Sensor.TYPE_GYROSCOPE:
                                sensorInfo.chineseName = "陀螺仪";
                                break;
                            case android.hardware.Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                                sensorInfo.chineseName = "陀螺仪未校准";
                                break;
                            case android.hardware.Sensor.TYPE_LIGHT:
                                sensorInfo.chineseName = "光感";
                                break;
                            case android.hardware.Sensor.TYPE_MAGNETIC_FIELD:
                                sensorInfo.chineseName = "磁力计";
                                break;
                            case android.hardware.Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                                sensorInfo.chineseName = "磁力计未校准";
                                break;
                            case android.hardware.Sensor.TYPE_PRESSURE:
                                sensorInfo.chineseName = "气压";
                                break;
                            case android.hardware.Sensor.TYPE_PROXIMITY:
                                sensorInfo.chineseName = "距离";
                                break;
                            case android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE:
                                sensorInfo.chineseName = "温度";
                                break;
                            case android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY:
                                sensorInfo.chineseName = "湿度";
                                break;
                            case android.hardware.Sensor.TYPE_ROTATION_VECTOR:
                                sensorInfo.chineseName = "旋转矢量";
                                break;
                            case android.hardware.Sensor.TYPE_SIGNIFICANT_MOTION:
                                sensorInfo.chineseName = "特殊动作";
                                break;
                            case android.hardware.Sensor.TYPE_STEP_COUNTER:
                                sensorInfo.chineseName = "计步器";
                                break;
                            case android.hardware.Sensor.TYPE_STEP_DETECTOR:
                                sensorInfo.chineseName = "步行检测";
                                break;
                            case android.hardware.Sensor.TYPE_ORIENTATION:
                                sensorInfo.chineseName = "方向传感";
                                break;
                            default:
                                sensorInfo.chineseName = "未知";
                                break;
                        }
                        return sensorInfo;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<SensorInfo>>() {
                    @Override
                    public void call(List<SensorInfo> sensorInfos) {
                        list.addAll(sensorInfos);
                        sendMsgShowTags(list);
                    }
                });
    }

    private void sendMsgShowTags(List<SensorInfo> list) {
        List temp = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            temp.add(list.get(i).chineseName);
        }
        MsgHelper.sendEvent(GlobalEventT.sensor_set_tags, "", temp);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null) {
            return;
        }
        if (event.sensor == null) {
            return;
        }
        if (event.sensor.getName().equals("")) {
            return;
        }
        int pos = LsListTransfer.getIndexWithSensorName(list, event.sensor.getName());
        if (pos < 0) {
            return;
        }
        int index = hashMap.get(list.get(pos).englishName);
        if (event.values == null) {
            sendMsgCardTip("none", index);
            return;
        }
        String temp = list.get(pos).englishName + "\n" + list.get(pos).chineseName + ":" + LsListTransfer.floatToString(event.values, 0, 3);
        sendMsgCardTip(temp, index);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void sendMsgCardTip(String tip, int index) {
        MsgHelper.sendEvent(GlobalEventT.sensor_set_card_info, tip, index);
    }

}
