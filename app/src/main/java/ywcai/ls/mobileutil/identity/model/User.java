package ywcai.ls.mobileutil.identity.model;

import android.util.Log;

import ywcai.ls.mobileutil.global.util.statics.MD5;

/**
 * Created by zmy_11 on 2017/7/15.
 */
public class User {
    public String userId;
    public String md5psw;
    public int loginChannel;//采用第三方认证登录将进入登录页面重走认证流程
    public String echo;//随机数
    public String sign;//数字签名，本地登录缓存文件只需要记录sign和随机数即可,算法有服务端生成，key由平台同一分配，每个ID唯一对应

    public void setSign() {
        LookDevice lookDevice = new LookDevice();
        String deviceId = lookDevice.getDeviceId();
        String deviceName = lookDevice.getDeviceName();
        echo = MD5.md5((int)Math.random()*100000+"");
        Log.i("ywcai", echo);
        sign = MD5.md5(userId + md5psw + loginChannel + echo + deviceId + deviceName + "@ywcai");
    }

    public boolean isVal() {
        LookDevice lookDevice = new LookDevice();
        String deviceId = lookDevice.getDeviceId();
        String deviceName = lookDevice.getDeviceName();
        String temp = MD5.md5(userId + md5psw + loginChannel + echo + deviceId + deviceName + "@ywcai");
        if (temp.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", md5psw='" + md5psw + '\'' +
                ", loginChannel=" + loginChannel +
                ", echo='" + echo + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
