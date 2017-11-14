package ywcai.ls.mobileutil.tools.Wifi.model;

public class WifiEntry {
    public String ssid="",bssid="",device="",keyType="",ip="",logFlag="";
    public int dbm=-160,channel=1,frequency,speed=0;
    public boolean isConnWifi=false,is2G=true,isShowInChart=true;


    @Override
    public String toString() {
        return "WifiEntry{" +
                "ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", device='" + device + '\'' +
                ", keyType='" + keyType + '\'' +
                ", ip='" + ip + '\'' +
                ", logFlag='" + logFlag + '\'' +
                ", dbm=" + dbm +
                ", channel=" + channel +
                ", frequency=" + frequency +
                ", speed=" + speed +
                ", isConnWifi=" + isConnWifi +
                ", is2G=" + is2G +
                ", isShowInChart=" + isShowInChart +
                '}';
    }


    public void initConnWifi()
    {
        bssid = "-1";
        ip = "-1";
        speed = -1;
    }

}
