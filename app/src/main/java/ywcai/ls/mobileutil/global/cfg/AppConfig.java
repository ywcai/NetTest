package ywcai.ls.mobileutil.global.cfg;

import ywcai.ls.mobileutil.R;

public class AppConfig {

    public static final String TENCENT_AD_ID = "9090328858666334";
    public static final String TENCENT_APP_ID = "1106630142";

    public static final String TIP_FOR_REMOTE_SAVE = "暂时不支持云端保存，请见谅";
    public static final String STR_LOCAL_MAC_FILE = "/sys/class/net/wlan0/address";
    public static final String STR_ARP_FILE_PATH = "/proc/net/arp";

    public static final String MAIN_ACTIVITY_PATH = "/main/view/MainActivity";
    public static final String WAIT_ACTIVITY_PATH = "/welcome/view/WaitActivity";

    public static final String PING_ACTIVITY_PATH = "/tools/Ping/view/PingActivity";
    public static final String WIFI_ACTIVITY_PATH = "/tools/Wifi/view/WifiActivity";
    public static final String STATION_ACTIVITY_PATH = "/tools/Station/view/StationActivity";
    public static final String SCAN_LAN_ACTIVITY_PATH = "/tools/ScanLan/view/ScanLanActivity";
    public static final String SCAN_PORT_ACTIVITY_PATH = "/tools/ScanPort/view/ScanPortActivity";
    public static final String BLE_ACTIVITY_PATH = WAIT_ACTIVITY_PATH;
    public static final String GPS_ACTIVITY_PATH = WAIT_ACTIVITY_PATH;
    public static final String SENSOR_ACTIVITY_PATH = "/tools/Sensor/view/SensorActivity";
    public static final String ORIENTATION_ACTIVITY_PATH = "/tools/Orientation/OrientationActivity";
    public static final String SPEED_ACTIVITY_PATH = "/tools/Speed/view/SpeedActivity";


    public static final String ROUTER_FLAG = "ROUTER_PAGE";


    public static long INT_WIFI_AUTO_SCAN_REFRESH = 5000;//如果没有自动接收到wifi事件，程序启动循环监听事件的间隔。
    public static long INT_CHECK_WIFI_AUTO_SCAN_COUNT = 5;//如果自动接收到了5次wifi事件，则判断该手机系统会自动刷新进行监听。


    public static final int INT_NOTIFICATION_PID_PING = 7294;
    public static final int INT_NOTIFICATION_PID_WIFI = 7295;
    public static final int INT_NOTIFICATION_PID_STATION = 7296;
    public static final int INT_NOTIFICATION_PID_SCAN_PORT = 7297;

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
    public static final int INDEX_PORT = 4;
    public static final int INDEX_SPEED = 5;
    public static final int INDEX_SENSOR = 6;
    public static final int INDEX_GPS = 7;
    public static final int INDEX_ORIENTATION = 8;
    public static final int INDEX_BLE = 9;


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
            R.color.chartLineColor9,
            R.color.chartLineColor10,
            R.color.chartLineColor11,
            R.color.chartLineColor12
    };
    public static final int[] colors2 = new int[]{
            R.color.chartLineColor1,
            R.color.chartLineColor3,
            R.color.chartLineColor6,
            R.color.chartLineColor9,
            R.color.chartLineColor0,

    };


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
                case INDEX_LAN:
                    temp[i] = SCAN_LAN_ACTIVITY_PATH;
                    break;
                case INDEX_BLE:
                    temp[i] = BLE_ACTIVITY_PATH;
                    break;
                case INDEX_PORT:
                    temp[i] = SCAN_PORT_ACTIVITY_PATH;
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
                    temp[i] = R.drawable.homepage_menu_scan_lan;
                    break;
                case INDEX_PORT:
                    temp[i] = R.drawable.homepage_menu_scan_port;
                    break;
                case INDEX_GPS:
                    temp[i] = R.drawable.homepage_menu_gps;
                    break;
                case INDEX_SENSOR:
                    temp[i] = R.drawable.homepage_menu_sensor;
                    break;
                case INDEX_SPEED:
                    temp[i] = R.drawable.homepage_menu_speed;
                    break;
                case INDEX_ORIENTATION:
                    temp[i] = R.drawable.homepage_menu_orientation;
                    break;
            }
        }
        return temp;
    }


    public static final String HTTP_TEST_BASE_URL = "http://p.gdown.baidu.com/";
    public static final String HTTP_TEST_BASE_URL_2 = "http://imtt.dd.qq.com";
    public static final String HTTP_APP_CONFIG_URL = "https://119.6.204.54/";

    public static final String[] HTTP_TEST_URLS = new String[]
            {
                    AppConfig.HTTP_TEST_1,
                    AppConfig.HTTP_TEST_2,
                    AppConfig.HTTP_TEST_3,
                    AppConfig.HTTP_TEST_4,
                    AppConfig.HTTP_TEST_5,
                    AppConfig.HTTP_TEST_6,
                    AppConfig.HTTP_TEST_7,
                    AppConfig.HTTP_TEST_8,
                    AppConfig.HTTP_TEST_9
            };

    public static final String[] HTTP_READ_URLS = new String[]
            {
                    AppConfig.HTTP_READ_WEB_0,
                    AppConfig.HTTP_READ_WEB_1,
                    AppConfig.HTTP_READ_WEB_2,
                    AppConfig.HTTP_READ_WEB_3,
                    AppConfig.HTTP_READ_WEB_4,
                    AppConfig.HTTP_READ_WEB_5,
                    AppConfig.HTTP_READ_WEB_6,
                    AppConfig.HTTP_READ_WEB_7,
                    AppConfig.HTTP_READ_WEB_8,
                    AppConfig.HTTP_READ_WEB_9
            };
    //    public static final String HTTP_TEST_1 = "http://p.gdown.baidu.com/f09287872f321491812d7632055be79e1d3a01b8dbc741f6049e8fe8f79d2c38a0b884344ae8908652651f73c5d0e634ae84a19c5e6e93b50ca337a73035f41dea373b0966ae57a30fe0d425a13693ab9306694acea457e312cf8d50ae6e5b8385895c39fe2cfc061cf4913986497d8ee99370402c8f1340da991e7527b8aa48b8420f96f1a4d67c";
    public static final String HTTP_TEST_1 = "http://imtt.dd.qq.com/16891/86A54AE973137F4665DA4E33A06518D6.apk?fsname=ywcai.ls.mobileutil_1.1.1_12.apk&csr=1bbd";
    public static final String HTTP_TEST_2 = "http://p.gdown.baidu.com/f09287872f321491812d7632055be79e1d3a01b8dbc741f6049e8fe8f79d2c38a0b884344ae8908652651f73c5d0e634ae84a19c5e6e93b50ca337a73035f41dea373b0966ae57a30fe0d425a13693ab9306694acea457e312cf8d50ae6e5b8385895c39fe2cfc061cf4913986497d8ee99370402c8f1340da991e7527b8aa48b8420f96f1a4d67c";
    public static final String HTTP_TEST_3 = "http://p.gdown.baidu.com/f3ddb5b75713c6aae88856003f5b573cadc7074a5f8a07b83f336c13312f2e95766375cbe7a035c4d62a818fe68d9c48830268fe1a265080bf223cdbc854cbb34cba74a6fa00bb96376acf2ce6024c2bc05b88551b5a468778391fd4860df2b381c37a1af47a33929515a4c9d07fd4b9b446b6e0242082e45bc08ec4ad9ceac9d4de19a4efa636cf98e063871e2de34637bfc2757d4dc8b460b786a520be597a5dd78ebbfdfd9ccd0e63cf667ae22c0cda24946679b93a05588b5410ff1a8165141cf0cf4368df61d2976fc62c8268b71ad2060a086c9df97293a498e0c16d7336ba7e6c245798d512b02b503963b4ad7613c9051ba34b1446afcc1773a8e4859785d48afc57306ef341d9c292426c29a813f01ed5fed403";
    public static final String HTTP_TEST_4 = "http://p.gdown.baidu.com/aeca3735594f4a7c3f49fcc64f3bb551badc4eb2b5582fd6c81173ca7aae84c07e1d91246dff8f470f6c6d9cbe8b7b897154eb43c693cd18a30801f317f7f1768d9d6eee5b45b65927df7fa38977f701c6cc9d1a9ddb6ea31bc552e4fb8252d3628c8f164468e7a7ecece0349d0f3e2013809f8c37646b6b2bfd5fb826ceadf97302a664d34259545cbac15a710bf366a71e88db4e34c4501b9400f371eed15b84467127f423bfb243479050710b9cbf4ef1fdadb844b9d76989908ba5819ac016944fcc20d88dd9bfc69c4eb7f8dd7e";
    public static final String HTTP_TEST_5 = "http://p.gdown.baidu.com/66c96b88d67c2c4800cc24435fbd632f99ce76c749b281284fdcda2e3f4ce11aaab0cd35ac1f35c4d9972e06d7a67731b8eecc50e387af2448cb8e3acef6ac5e80f5a1b55d7b565b4678bf4a478200e1b38a21401146eba0948af009cf585b07c2260312ddab9ef76b8b8ab71da40341aa7072066ab549326d8281c8f2c89184ba2af1beff23ce0e7f8de8c9aecc50303c5eb560b7cd025db6fd8800981d2efd1538926fee90cd0048a18d68309c8b5eebbc6855ddf9e6e91f81f8ad8c5fac97c88089d8276945070934b7b80d0db0b68086cff00bb51290c588f0a83c385b49af99c89553d5b8abaf9165e235c75e6cea4f75e56d67cd5c";
    public static final String HTTP_TEST_6 = "http://p.gdown.baidu.com/197ae4dd4a0b5f9fc1250aa4f4faf971e8fd5528da772b2519c75d2315fcadb51f7470708fe30791039961fcc3c8782574ef4d358f3e8f50ec4d1d192d2e44b462fca7bdfd63c6280b3fb0d235c489c08873ecbbaa09562d43ce7ed34714aa8a";
    public static final String HTTP_TEST_7 = "http://p.gdown.baidu.com/c24f522b4e72c190f4539c23fe718d576d7750acd0070f8f3ec8926bcc373b919e703e18edafdea3a06ca38e0372a42a3b4ca9699ba86a0b88188bc62a1afbdea0d8def75b4b453418f0d8ea4c04dead760eba7eea9d2e0df1eb5e2be963b3d2c1250aa4f4faf971780551c24a0ce8138f357672f23e998893b6847b21c6fa89039961fcc3c8782574ef4d358f3e8f50bfca62c0135de8483a5c2a00130d319f2bbf5cfcee5af7e1176d4f77c901ceea82aa4740882e7f34";
    public static final String HTTP_TEST_8 = "http://p.gdown.baidu.com/972a97f53505c320ae180d127c3af6b48e84388a4d92ac8e17dff4cc204e5277a6c0917c3c72925067a2c9e133fe4253d259ef6afa25e591ce114c00857e5b3f4dd4e14dc8d79310dee3856ecea0afbcebc9dd9ec51a626423e8f436a821c289ad32e916987f636a7634c157f2868f9c7f620801a96732e534578ff526131404a6e59327b963e611dc70bbfbda42337d642966fbdd4d8484";
    public static final String HTTP_TEST_9 = "http://p.gdown.baidu.com/2649bbc20d21a6865d06c9a74183f4b9d63f96453a74b412368c4c23a13921e18e5ddd3e33adaf9e909d6a846ea9baa0c9b7fe2a70fbd725aa3e4fc1fa6e322b24dba27ee4b409f0a413432a59f6efffccf29907e56f421029c082a780fa64482fa778d2fc0c05004fe01dab32bade7c0ce5eb99ec2910232db35395ac3d161fe625e5f785770404b8a0a4cfe44d5e36b24c78155cccc8ffb60879317b31af2350ef48c34e620d0845ab82f8dea94fac6bf0caac98238e8fa6af6af46d299b1e4e8368e7de817d311261ea6e92f57bc1a45288e0c7775de67fa8b5bf6dc90b57a56b2560f417ca2dc1250aa4f4faf9715d928b4c5b510198750a3e224144f1fe549e5e8402b9306b039961fcc3c8782574ef4d358f3e8f50f783faa20632fd1c1239cd7a22be348cfab7e74c9c1603b6bf3c45e4e393c3bc8a61e3626a899d9a";

    public static final String HTTP_READ_WEB_0 = "http://www.sina.com.cn/";
    public static final String HTTP_READ_WEB_1 = "https://www.baidu.com/";
    public static final String HTTP_READ_WEB_2 = "https://www.taobao.com/";
    public static final String HTTP_READ_WEB_3 = "http://www.qq.com/";
    public static final String HTTP_READ_WEB_4 = "https://www.sohu.com/";
    public static final String HTTP_READ_WEB_5 = "http://www.189.cn/";
    public static final String HTTP_READ_WEB_6 = "https://www.aliyun.com/";
    public static final String HTTP_READ_WEB_7 = "https://www.jd.com/";
    public static final String HTTP_READ_WEB_8 = "http://shouji.baidu.com/software/23005641.html";
    public static final String HTTP_READ_WEB_9 = "http://shouji.baidu.com/s?wd=%E5%A4%9A%E7%BB%B4%E7%BD%91%E7%BB%9C%E5%88%86%E6%9E%90&data_type=app&f=header_app%40input%40btn_search";
}

