package ywcai.ls.mobileutil.global.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by zmy_11 on 2017/12/14.
 */

public class Ping {
    public int pingLength = 200;

    public float pingCmd(String ip) {
        //LOSS的包在坐标轴中表示的刻度。
        float y = -40;
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 -n -s " + pingLength + " " + ip);
            new BufferedReader(new InputStreamReader(p.getErrorStream()));
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int status = p.waitFor();
            if (status == 0) {
                String str = "";
                while ((str = buf.readLine()) != null) {
                    if (str.indexOf("icmp_seq=") >= 0 && str.indexOf("time=") >= 0) {
                        String delay = str.split("=")[3].split(" ")[0];
                        y = Float.parseFloat(delay);
                        return y;
                    }
                }
            }
        } catch (Exception e) {

        }
        return y;
    }
}
