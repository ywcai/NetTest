package ywcai.ls.mobileutil.tools.Speed.model;


import ywcai.ls.mobileutil.tools.Speed.model.inf.DownService;

/**
 * Created by zmy_11 on 2017/12/23.
 */

public class ReadRunnable implements Runnable {
    SpeedState speedState;
    int index;
    HttpDown http;

    public ReadRunnable(SpeedState speedState, int index, DownService downService) {
        this.speedState = speedState;
        this.index = index % 10;
        http = new HttpDown(speedState, downService);
    }

    @Override
    public void run() {
        http.executeRead(index);
    }
}
