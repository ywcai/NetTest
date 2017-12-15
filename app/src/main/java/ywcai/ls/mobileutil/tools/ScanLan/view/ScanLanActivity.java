package ywcai.ls.mobileutil.tools.ScanLan.view;

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

import ywcai.ls.mobileutil.tools.ScanLan.model.ScanLanResult;
import ywcai.ls.mobileutil.tools.ScanLan.presenter.ScanLanAction;

/**
 * Created by zmy_11 on 2017/11/28.
 */
@Route(path = "/tools/ScanLan/view/ScanLanActivity")
public class ScanLanActivity extends AppCompatActivity {
    //    private MaterialDialog bottomDialog;
    private ImageView btnRefresh;
    private LsScan lsScan;
    private TextView textLocalIp;


    ScanLanAction action = new ScanLanAction();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_scan_lan);
        InitToolBar();
        InitView();
    }


    private void InitView() {
        textLocalIp = (TextView) findViewById(R.id.scan_lan_local_ip);
        lsScan = (LsScan) findViewById(R.id.radarScanView);
        btnRefresh = (ImageView) findViewById(R.id.scan_lan_btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.INVISIBLE);
                lsScan.startScan();
                action.startWork();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        action.stopScanThread();
        super.onDestroy();
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.scan_lan_toolbar);
        mToolbar.setTitleMarginStart(0);
        mToolbar.setTitle(AppConfig.TITLE_LAN);
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
            case GlobalEventT.scan_lan_ping_index_result:
                updateRadar((ScanLanResult) event.obj);
                break;
            case GlobalEventT.scan_lan_refresh_local_ip:
                updateText(event.tip);
                break;
            case GlobalEventT.scan_lan_ping_stop:
                updateRadarStop();
                break;
        }

    }

    private void updateRadarStop() {
        lsScan.stopScan();
        btnRefresh.setVisibility(View.VISIBLE);
    }

    private void updateRadar(ScanLanResult scanLanResult) {
        //大于等于零标识PING成功
        if (scanLanResult.pingResult >= 0) {
            //半径
            Double radius = (scanLanResult.index % 5 + 1) * 1.00 / 6;
            //弧度
            int rate = (new Random().nextInt(scanLanResult.max) + 1) * 360 / scanLanResult.max;
            lsScan.addText("[" + scanLanResult.index + "]", radius, rate, 18, Color.YELLOW);
        }
        lsScan.setProgress(scanLanResult.count * 100 / scanLanResult.max);
    }

    private void updateText(String tip) {
        textLocalIp.setText(tip);
    }

}
