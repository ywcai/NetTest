package ywcai.ls.mobileutil.tools.Wifi.view;


import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mobstat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import ywcai.ls.control.LoadingDialog;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiPageAdapter;
import ywcai.ls.mobileutil.tools.Wifi.presenter.MainWifiAction;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;

@Route(path = "/tools/Wifi/view/WifiActivity")
public class WifiActivity extends AppCompatActivity {
    private int nowPage = 1;
    private List<ImageView> list = new ArrayList<>();
    private MainWifiActionInf mainWifiActionInf;
    private RelativeLayout snack_container;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_wifi);
        InitMainAction();
        InitToolBar();
        InitControlBtn();
        InstallFragment();
        loadingDialog = new LoadingDialog(this);
    }

    private void InitControlBtn() {
        FancyButton btn2d4G = (FancyButton) findViewById(R.id.btn_2d4g);
        FancyButton btn5G = (FancyButton) findViewById(R.id.btn_5g);
        btn2d4G.setGhost(true);
        btn2d4G.setEnabled(false);
        btn5G.setGhost(false);
        btn5G.setEnabled(true);
        btn2d4G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainWifiActionInf.set2d4G(true);
            }
        });
        btn5G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainWifiActionInf.set2d4G(false);
            }
        });
        snack_container = (RelativeLayout) findViewById(R.id.snack_container);
    }

    private void InitMainAction() {
        mainWifiActionInf = new MainWifiAction();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.wifi_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InstallFragment() {
        ImageView icon1 = (ImageView) findViewById(R.id.icon_1);
        ImageView icon2 = (ImageView) findViewById(R.id.icon_2);
        ImageView icon3 = (ImageView) findViewById(R.id.icon_3);
        ImageView icon4 = (ImageView) findViewById(R.id.icon_4);
        list.add(icon1);
        list.add(icon2);
        list.add(icon3);
        list.add(icon4);
        WifiPageAdapter wifiPageAdapter = new WifiPageAdapter(this.getSupportFragmentManager(), mainWifiActionInf);
        ViewPager wifiViewPager = (ViewPager) findViewById(R.id.wifiViewPager);
        wifiViewPager.setOffscreenPageLimit(3);
        wifiViewPager.setAdapter(wifiPageAdapter);
        wifiViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        changeIcon(0);
    }

    private void changeIcon(int position) {
        list.get(nowPage).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.free_page));
        list.get(position).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.busy_page));
        nowPage = position;
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateDeviceList(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.wifi_set_main_title_tip:
                TextView textView = (TextView) findViewById(R.id.wifi_toolbar_tip);
                textView.setText(event.tip);
                break;
            case GlobalEventT.wifi_set_channel_btn_status:
                set2d4gBtnStatus((Boolean) event.obj);
                break;
            case GlobalEventT.global_pop_snack_tip:
                closeLoading();
                showBottomTip(event.tip, ((boolean) event.obj));
                break;
            case GlobalEventT.global_pop_loading_dialog:
                popLoading(event.tip);
                break;
        }
    }

    private void closeLoading() {
        loadingDialog.dismiss();
    }

    private void popLoading(String tip) {
        loadingDialog.setLoadingText(tip);
        loadingDialog.show();
    }

    private void set2d4gBtnStatus(Boolean a2d4gBtnStatus) {
        FancyButton btn2d4G = (FancyButton) findViewById(R.id.btn_2d4g);
        FancyButton btn5G = (FancyButton) findViewById(R.id.btn_5g);
        btn2d4G.setGhost(a2d4gBtnStatus);
        btn2d4G.setEnabled(!a2d4gBtnStatus);
        btn5G.setGhost(!a2d4gBtnStatus);
        btn5G.setEnabled(a2d4gBtnStatus);
        btn2d4G.setVisibility(View.VISIBLE);
        btn5G.setVisibility(View.VISIBLE);
    }

    private void showBottomTip(String tip, boolean success) {
        if (success) {
            LsSnack.show(this, snack_container, tip, -1);
        } else {
            LsSnack.show(this, snack_container, tip, R.color.LRed);
        }
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
}
