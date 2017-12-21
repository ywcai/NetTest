package ywcai.ls.mobileutil.tools.Sensor.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mobstat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.control.flex.FlexButtonLayout;
import ywcai.ls.control.flex.OnFlexButtonClickListener;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.LsListTransfer;
import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.Sensor.presenter.SensorAction;
import ywcai.ls.mobileutil.tools.Sensor.presenter.inf.SensorActionInf;

@Route(path = "/tools/Sensor/view/SensorActivity")
public class SensorActivity extends AppCompatActivity {

    private FlexButtonLayout flexButtonLayout;
    private SensorActionInf sensorActionInf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_sensor);
        initAction();
        InitToolBar();
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSensor(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.sensor_recovery_tag_state:
                setTags((int[]) event.obj);
                break;
            case GlobalEventT.sensor_set_snack_tip:
                popSnackBar(event.tip);
                break;
        }

    }

    private void popSnackBar(String tip) {
        RelativeLayout snack_container = (RelativeLayout) findViewById(R.id.sensor_container);
        LsSnack.show(this, snack_container, tip, Color.RED);
    }

    private void setTags(int[] tags) {
        flexButtonLayout.setSelectIndex(tags);
    }

    private void initAction() {
        sensorActionInf = new SensorAction();
        sensorActionInf.checkDeviceSensors(this);
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.sensor_toolbar);
        mToolbar.setTitleMarginStart(0);
        mToolbar.setTitle(AppConfig.TITLE_SENSOR);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        flexButtonLayout = (FlexButtonLayout) findViewById(R.id.sensor_list);
        flexButtonLayout.setOnFlexButtonClickListener(new OnFlexButtonClickListener() {
            @Override
            public void clickItem(int i, boolean b) {
                popSnackBar(LsListTransfer.intToString(flexButtonLayout.getSelectIndex()));
//                sensorActionInf.clickSensorTag(flexButtonLayout.getSelectIndex());
            }

            @Override
            public void clickAllBtn(int[] ints, boolean b) {

            }
        });
        int[] temp = new int[flexButtonLayout.getSelectIndex().length];
        flexButtonLayout.setSelectIndex(temp);
        List test = new ArrayList();
        test.add("tag1");
        test.add("tag2");
        test.add("tag3");
        test.add("tag4");
        test.add("tag5");
        test.add("tag6");
        flexButtonLayout.setDataAdapter(test);
    }


    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
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
}
