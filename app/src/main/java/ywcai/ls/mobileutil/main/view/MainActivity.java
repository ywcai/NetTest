package ywcai.ls.mobileutil.main.view;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.baidu.mobstat.StatService;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;
import java.util.ArrayList;
import java.util.List;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.menu.view.MenuFragment;
import ywcai.ls.mobileutil.results.view.ResultFragment;
import ywcai.ls.mobileutil.setting.SettingFragment;

@Route(path = "/main/view/MainActivity")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Fragment> fragments = new ArrayList<>();
    private List<TextView> nav = new ArrayList<>();
    public int currentPage = 3;
    private final String bannerId = "1060320980023408";
    private final String appID = "1106630142";
    BannerView banner;
    @Autowired()
    public String ROUTER_PAGE;
    private ProgressDialog progressDialog;
    RelativeLayout ad_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        SetTitle.setTitleTransparent(getWindow());
        setContentView(R.layout.activity_main);
        checkApkVersion();
        InstallFragment();
        selectFragment(0);
        startCacheActivity();
        initAD();
        banner.loadAD();
        banner.setShowClose(true);
    }

    private void initAD() {
        ad_container = (RelativeLayout) findViewById(R.id.main_ad_container);
        banner = new BannerView(this, ADSize.BANNER, appID, bannerId);
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(0);
        banner.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {

            }

            @Override
            public void onADReceiv() {

            }
        });
        ad_container.addView(banner);

    }

    private void checkApkVersion() {
        progressDialog = new ProgressDialog(this);
        BDAutoUpdateSDK.uiUpdateAction(MainActivity.this, new MyUICheckUpdateCallback());
    }

    private void startCacheActivity() {
        //如果不是从桌面第一次打开，则根据缓存的页面进行跳转，暂时没有引用
        if (!ROUTER_PAGE.equals("First"))
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ARouter.getInstance().build(ROUTER_PAGE)
                            .navigation();
                }
            }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.startActivity(startIntent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bot_nav_1:
                selectFragment(0);
                break;
            case R.id.bot_nav_2:
                selectFragment(1);
                break;
            case R.id.bot_nav_3:
                selectFragment(2);
                break;
            case R.id.bot_nav_4:
                selectFragment(3);
                break;
        }
    }

    private void InstallFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MenuFragment netWorkFragment = new MenuFragment();
        ResultFragment resultFragment = new ResultFragment();
        Fragment reserveFragment2 = new Fragment();
        Fragment reserveFragment3 = new SettingFragment();
        transaction.add(R.id.main_fragment_container, netWorkFragment);
        transaction.add(R.id.main_fragment_container, resultFragment);
        transaction.add(R.id.main_fragment_container, reserveFragment2);
        transaction.add(R.id.main_fragment_container, reserveFragment3);
        fragments.add(netWorkFragment);
        fragments.add(resultFragment);
        fragments.add(reserveFragment2);
        fragments.add(reserveFragment3);
        transaction.hide(netWorkFragment);
        transaction.hide(resultFragment);
        transaction.hide(reserveFragment2);
        transaction.hide(reserveFragment3);
        transaction.commit();
        TextView nav_1 = (TextView) findViewById(R.id.bot_nav_1);
        TextView nav_2 = (TextView) findViewById(R.id.bot_nav_2);
        TextView nav_3 = (TextView) findViewById(R.id.bot_nav_3);
        TextView nav_4 = (TextView) findViewById(R.id.bot_nav_4);
        nav_1.setOnClickListener(this);
        nav_2.setOnClickListener(this);
        nav_3.setOnClickListener(this);
        nav_4.setOnClickListener(this);
        nav.add(nav_1);
        nav.add(nav_2);
        nav.add(nav_3);
        nav.add(nav_4);
        nav_1.setTextColor(0xFF666967);
        nav_1.setTextSize(12);
        nav_2.setTextColor(0xFF666967);
        nav_2.setTextSize(12);
        nav_3.setTextColor(0xFF666967);
        nav_3.setTextSize(12);
        nav_4.setTextColor(0xFF666967);
        nav_4.setTextSize(12);
    }

    private void selectFragment(int selectPage) {
        if (selectPage != currentPage) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.hide(fragments.get(currentPage));
            transaction.show(fragments.get(selectPage));
            transaction.commit();
            nav.get(currentPage).setTextColor(0xFF666967);
            nav.get(selectPage).setTextColor(0xFF3eb875);
            nav.get(currentPage).setTextSize(12);
            nav.get(selectPage).setTextSize(14);
            currentPage = selectPage;
        }
    }

    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {
        @Override
        public void onCheckComplete() {
            progressDialog.dismiss();
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
