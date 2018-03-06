package ywcai.ls.mobileutil.tools.Orientation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mobstat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.LsToolbar;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;


@Route(path = "/tools/Orientation/OrientationActivity")
public class OrientationActivity extends AppCompatActivity {
    RelativeLayout rl;
    OrientationView orientationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_orientation);
        initView();
        InitToolBar();
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        SensorEventListener orientationListener = new OrientationListener();
        sensorManager.unregisterListener(orientationListener);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(orientationListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.orientation_container);
        orientationView = new OrientationView(this);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        orientationView.setLayoutParams(layout);
        rl.addView(orientationView, 0);
    }

    private void InitToolBar() {
        LsToolbar.initToolbar(this, AppConfig.TITLE_ORIENTATION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        StatService.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        StatService.onResume(this);
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateOrientation(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.orientation_refresh:
                setOrientation((float) event.obj);
                break;
        }
    }

    private void setOrientation(float rate) {
        orientationView.setRealTimeValue(rate);
    }
}
