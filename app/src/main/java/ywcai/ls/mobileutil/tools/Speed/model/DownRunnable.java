package ywcai.ls.mobileutil.tools.Speed.model;


/**
 * Created by zmy_11 on 2017/12/23.
 */

public class DownRunnable implements Runnable {
    SpeedState speedState;
    int index;
    HttpDown http;

    public DownRunnable(SpeedState speedState, int index) {
        this.speedState = speedState;
        this.index = index % 9;
        http = new HttpDown(speedState);
    }

    @Override
    public void run() {
        http.executeDownload(index);
    }
}
