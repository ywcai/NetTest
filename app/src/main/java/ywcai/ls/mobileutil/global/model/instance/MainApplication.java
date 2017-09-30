package ywcai.ls.mobileutil.global.model.instance;


import android.app.Application;

public class MainApplication extends Application {
    private static MainApplication instance;
    public static MainApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
            super.onCreate();
            instance = this;
    }
}
