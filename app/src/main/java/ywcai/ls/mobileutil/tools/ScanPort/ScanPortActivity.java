package ywcai.ls.mobileutil.tools.ScanPort;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import ywcai.ls.control.scan.LsScan;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.ScanLan.presenter.ScanLanAction;
import ywcai.ls.mobileutil.tools.ScanPort.presenter.ScanPortAction;
import ywcai.ls.mobileutil.tools.ScanPort.presenter.inf.ScanPortActionInf;

/**
 * Created by zmy_11 on 2017/11/28.
 */
@Route(path = "/tools/ScanPort/view/ScanPortActivity")
public class ScanPortActivity extends AppCompatActivity {

    private ImageView btnRefresh;
    private LsScan lsScan;
    private TextView textLocalIp;

    ScanPortActionInf actionInf = new ScanPortAction();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_scan_port);
        InitToolBar();
        InitView();
    }


    private void InitView() {
        textLocalIp = (TextView) findViewById(R.id.scan_port_local_ip);
        lsScan = (LsScan) findViewById(R.id.radarScanPortView);
        btnRefresh = (ImageView) findViewById(R.id.scan_port_btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.INVISIBLE);
                lsScan.startScan();
                actionInf.startScanPort();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        actionInf.stopScanPort();
        super.onDestroy();
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.scan_port_toolbar);
        mToolbar.setTitleMarginStart(0);
        mToolbar.setTitle(AppConfig.TITLE_PORT);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(GlobalEvent event) {
        switch (event.type) {

        }

    }

    private void updateRadarEnd() {
        lsScan.stopScan();
        btnRefresh.setVisibility(View.VISIBLE);
    }

    private void updateRadar(String tip, float obj) {
        //需要通过接受到的数据判断是否断开
        actionInf.scanPortEnd();

    }

    private void updateText(String tip) {
        textLocalIp.setText(tip);
    }

}
