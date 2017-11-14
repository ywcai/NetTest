package ywcai.ls.mobileutil.global.model;

import java.util.concurrent.ThreadFactory;

/**
 * Created by zmy_11 on 2017/10/2.
 */

public class LsThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        Thread t =new Thread(r);
        t.setDaemon(true);
        return t;
    }
}
