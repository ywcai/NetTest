package ywcai.ls.mobileutil.global.cfg;

import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2017/10/6.
 */

public class AppConfig {
    public static final String MAIN_ACTIVITY_PATH = "/main/view/MainActivity";

    public static final String PING_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";
    public static final String WIFI_ACTIVITY_PATH = "/tools/Wifi/view/WifiActivity";
    public static final String STATION_ACTIVITY_PATH = "/tools/Station/view/StationActivity";
    public static final String BLE_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";
    public static final String LAN_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";
    public static final String PORT_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";
    public static final String GPS_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";
    public static final String SENSOR_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";
    public static final String ORIENTATION_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";
    public static final String SPEED_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";

    public static final String MENU_FRAGMENT_PATH = "/main/view/menuFragment";

    public static final String ROUTER_FLAG = "ROUTER_PAGE";

    public static long INT_WIFI_AUTO_SCAN_REFRESH = 10000;//如果没有自动接收到wifi事件，程序启动循环监听事件的间隔。
    public static long INT_CHECK_WIFI_AUTO_SCAN_COUNT = 5;//如果自动接收到了5次wifi事件，则判断该手机系统会自动刷新进行监听。


    public static final int INT_NOTIFICATION_PID_PING=7294;
    public static final int INT_NOTIFICATION_PID_WIFI=7295;

    public static final String TITLE_PING = "PING";
    public static final String TITLE_WIFI = "WIFI";
    public static final String TITLE_STATION = "基站信号";
    public static final String TITLE_BLE = "蓝牙扫描";
    public static final String TITLE_LAN = "内网扫描";
    public static final String TITLE_PORT = "端口扫描";
    public static final String TITLE_GPS = "经纬度";
    public static final String TITLE_SENSOR = "传感器";
    public static final String TITLE_ORIENTATION = "指南针";
    public static final String TITLE_SPEED = "网络测速";
    public static final int INDEX_PING = 0;
    public static final int INDEX_WIFI = 1;
    public static final int INDEX_STATION = 2;
    public static final int INDEX_LAN = 3;
    public static final int INDEX_SENSOR = 4;
    public static final int INDEX_PORT = 5;
    public static final int INDEX_GPS = 6;
    public static final int INDEX_BLE = 7;
    public static final int INDEX_SPEED = 8;
    public static final int INDEX_ORIENTATION = 9;

    public static final int[] INTS_CHANNEL_2D4G = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    public static final int[] INTS_CHANNEL_5G = new int[]{149, 153, 157, 161, 165};

    public static final int[] colors = new int[]{
            R.color.chartLineColor0,
            R.color.chartLineColor1,
            R.color.chartLineColor2,
            R.color.chartLineColor3,
            R.color.chartLineColor4,
            R.color.chartLineColor5,
            R.color.chartLineColor6,
            R.color.chartLineColor7,
            R.color.chartLineColor8,
            R.color.chartLineColor9
    };

//    public static final String[] menuString = new String[]
//            {
//                    TITLE_PING,
//                    TITLE_WIFI,
//                    TITLE_STATION
////                    TITLE_BLE,
////                    TITLE_LAN,
////                    TITLE_PORT,
////                    TITLE_GPS,
////                    TITLE_SENSOR,
////                    TITLE_ORIENTATION,
////                    TITLE_SPEED
//            };


    //本地最多允许存储500条测试结果记录。
    public final int MAX_LOCAL_SAVE = 500;

    public static String[] getMenuTextStr() {
        String[] temp = new String[10];
        for (int i = 0; i < temp.length; i++) {
            switch (i) {
                case INDEX_PING:
                    temp[i] = TITLE_PING;
                    break;
                case INDEX_WIFI:
                    temp[i] = TITLE_WIFI;
                    break;
                case INDEX_STATION:
                    temp[i] = TITLE_STATION;
                    break;
                case INDEX_BLE:
                    temp[i] = TITLE_BLE;
                    break;
                case INDEX_LAN:
                    temp[i] = TITLE_LAN;
                    break;
                case INDEX_PORT:
                    temp[i] = TITLE_PORT;
                    break;
                case INDEX_GPS:
                    temp[i] = TITLE_GPS;
                    break;
                case INDEX_SENSOR:
                    temp[i] = TITLE_SENSOR;
                    break;
                case INDEX_SPEED:
                    temp[i] = TITLE_SPEED;
                    break;
                case INDEX_ORIENTATION:
                    temp[i] = TITLE_ORIENTATION;
                    break;
            }
        }
        return temp;
    }


    public static String[] getMenuUrlStr() {
        String[] temp = new String[10];
        for (int i = 0; i < temp.length; i++) {
            switch (i) {
                case INDEX_PING:
                    temp[i] = PING_ACTIVITY_PATH;
                    break;
                case INDEX_WIFI:
                    temp[i] = WIFI_ACTIVITY_PATH;
                    break;
                case INDEX_STATION:
                    temp[i] = STATION_ACTIVITY_PATH;
                    break;
                case INDEX_BLE:
                    temp[i] = BLE_ACTIVITY_PATH;
                    break;
                case INDEX_LAN:
                    temp[i] = LAN_ACTIVITY_PATH;
                    break;
                case INDEX_PORT:
                    temp[i] = PORT_ACTIVITY_PATH;
                    break;
                case INDEX_GPS:
                    temp[i] = GPS_ACTIVITY_PATH;
                    break;
                case INDEX_SENSOR:
                    temp[i] = SENSOR_ACTIVITY_PATH;
                    break;
                case INDEX_SPEED:
                    temp[i] = SPEED_ACTIVITY_PATH;
                    break;
                case INDEX_ORIENTATION:
                    temp[i] = ORIENTATION_ACTIVITY_PATH;
                    break;
            }
        }
        return temp;
    }

    public static int[] getMenuIconRes() {
        int[] temp = new int[10];
        for (int i = 0; i < temp.length; i++) {
            switch (i) {
                case INDEX_PING:
                    temp[i] = R.drawable.homepage_menu_ping;
                    break;
                case INDEX_WIFI:
                    temp[i] = R.drawable.homepage_menu_wifi;
                    break;
                case INDEX_STATION:
                    temp[i] = R.drawable.homepage_menu_station;
                    break;
                case INDEX_BLE:
                    temp[i] = R.drawable.homepage_menu_ble;
                    break;
                case INDEX_LAN:
                    temp[i] = R.drawable.homepage_menu_lan;
                    break;
                case INDEX_PORT:
                    temp[i] = R.drawable.homepage_menu_more;
                    break;
                case INDEX_GPS:
                    temp[i] = R.drawable.homepage_menu_gps;
                    break;
                case INDEX_SENSOR:
                    temp[i] = R.drawable.homepage_menu_sensor;
                    break;
                case INDEX_SPEED:
                    temp[i] = R.drawable.homepage_menu_orientation;
                    break;
                case INDEX_ORIENTATION:
                    temp[i] = R.drawable.homepage_menu_orientation;
                    break;
            }
        }
        return temp;
    }

}

