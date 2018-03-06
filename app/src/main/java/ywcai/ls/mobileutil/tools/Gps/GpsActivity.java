package ywcai.ls.mobileutil.tools.Gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.LocationService;

import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.LsToolbar;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;


@Route(path = "/tools/Gps/GpsActivity")
public class GpsActivity extends AppCompatActivity {
    private TextView tv_number, tv_j, tv_w, tv_g, tv_xyz, tv_speed, tv_addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_gps);
        InitView();
        InitToolBar();
        InitAction();
    }

    private void InitView() {
        tv_number = (TextView) findViewById(R.id.gps_j);
        tv_j = (TextView) findViewById(R.id.gps_j);
        tv_w = (TextView) findViewById(R.id.gps_w);
        tv_g = (TextView) findViewById(R.id.gps_g);
        tv_xyz = (TextView) findViewById(R.id.gps_xyz);
        tv_speed = (TextView) findViewById(R.id.gps_speed);
        tv_addr = (TextView) findViewById(R.id.gps_addr);
    }

    private void InitToolBar() {
        LsToolbar.initToolbar(this, AppConfig.TITLE_GPS);
    }

    private void InitAction() {
        final LocationService locationService = new LocationService(MainApplication.getInstance().getApplicationContext());
        locationService.registerListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                updateUi(bdLocation);
            }
        });
        locationService.start();
    }

    private void updateUi(BDLocation bdLocation) {
        tv_number.setText(bdLocation.getSatelliteNumber() + "");
        tv_j.setText(bdLocation.getLongitude() + "");
        tv_w.setText(bdLocation.getAltitude() + "");
        tv_g.setText(bdLocation.getLatitude() + "");
        tv_xyz.setText(bdLocation.toString() + "");
        tv_speed.setText(bdLocation.getSpeed() + "");
        tv_addr.setText(bdLocation.getAddrStr() + "");
    }
}
