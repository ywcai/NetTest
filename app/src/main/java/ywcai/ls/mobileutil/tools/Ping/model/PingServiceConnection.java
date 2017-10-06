//package ywcai.ls.mobileutil.tools.Ping.model;
//
//import android.content.ComponentName;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//
//import rx.Observable;
//import rx.Subscriber;
//import ywcai.ls.mobileutil.tools.Ping.presenter.PingService;
//
///**
// * Created by zmy_11 on 2017/8/29.
// */
//
//public class PingServiceConnection  implements ServiceConnection {
//    public PingService pingService;
//    private Subscriber subscriber;
//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        pingService = ((PingService.MyBinder) service).getPingService();
//        Observable.just(subscriber);
//    }
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//        pingService=null;
//        Observable.just(subscriber);
//    }
//    public  PingServiceConnection()
//    {
//
//    }
//}