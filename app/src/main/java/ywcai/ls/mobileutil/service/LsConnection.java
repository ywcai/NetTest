package ywcai.ls.mobileutil.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zmy_11 on 2017/10/10.
 */

public class LsConnection implements ServiceConnection {
    Service tempService;
    Action1 action1;
    public LsConnection(Action1 action1)
    {
        this.action1=action1;
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        tempService=((MyBinder)service).getService();
        Observable.just(tempService).subscribe(action1);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        tempService=null;
        Observable.just(tempService).subscribe(action1);
    }
}
