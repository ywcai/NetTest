package ywcai.ls.mobileutil.global.model.instance;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import ywcai.ls.mobileutil.global.model.LocationService;

public class MainApplication extends Application {
    private static MainApplication instance;
    public LocationService locationService;
    public boolean isActivityExist = false;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //显示奔溃日志
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(true) //default: true
                .showErrorDetails(true) //default: true
                .apply();
        instance = this;
        ARouter.init(this);
        locationService = new LocationService(getApplicationContext());
    }
}
