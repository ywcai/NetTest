package ywcai.ls.mobileutil.welcome.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mobstat.StatService;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.AppInfo;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.tools.Speed.model.inf.DownService;


public class WelComeActivity extends AppCompatActivity implements SplashADListener {
    public String ROUTER_PAGE = "First";
    SplashAD splashAD;
    ViewGroup adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取到的标识位与FLAG_ACTIVITY_BROUGHT_TO_FRONT进行与操作。如果获取到的标识位中含有FLAG_ACTIVITY_BROUGHT_TO_FRONT则不会为零，若不含则为零
        //这个比较类似TCP协议的标识位比较。不能转换为int型数据来看。
        boolean isBroughtToFront = (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0;
        boolean isNewTask = (getIntent().getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) != 0;
        //桌面入口重新点击程序
        if (isBroughtToFront && isNewTask) {
            finish();
            return;
        }
        //不存在实例，
        if (!MainApplication.getInstance().isActivityExist) {
                /*
                重建时，重绘welcome页面后再进入，否则会闪白屏，不是很美观
                 */
            showWelcomeActivity();
                /*
                路由信息，可根据上一次应用关闭时的TOP activity缓存来恢复
                 */
        } else {
            //已存在，直接关闭这次吊起的页面即可
            finish();
        }
        return;

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //显示欢迎页面，在这里加入欢迎页面的所有广告内容
    private void showWelcomeActivity() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_welcome);
        MainApplication.getInstance().isActivityExist = true;
        adContainer = (ViewGroup) findViewById(R.id.splash_container);
        splashAD = new SplashAD(WelComeActivity.this, adContainer, AppConfig.TENCENT_APP_ID, AppConfig.TENCENT_AD_ID, WelComeActivity.this);
    }

    private void requestAdConfig() {
        String baseUrl = AppConfig.HTTP_TEST_BASE_URL_2;
        String requestUrl = "http://119.6.204.54:8080/NetTest/appinfo";
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(baseUrl).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ConfigService service = retrofit.create(ConfigService.class);
        Call<ResponseBody> call = service.getAppInfo(requestUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //请求正常，直接进入主页面
                startMainActivity();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppInfo appInfo = new AppInfo();
                appInfo.isLoadAd = true;
                CacheProcess.getInstance().setAppInfo(appInfo);
                adContainer = (ViewGroup) findViewById(R.id.splash_container);
                splashAD = new SplashAD(WelComeActivity.this, adContainer, AppConfig.TENCENT_APP_ID, AppConfig.TENCENT_AD_ID, WelComeActivity.this);
            }
        });

    }

    //跳转到主界面
    private void startMainActivity() {
        ARouter.getInstance().
                build(AppConfig.MAIN_ACTIVITY_PATH).withString(AppConfig.ROUTER_FLAG, ROUTER_PAGE).
                navigation();
    }


    @Override
    public void onADDismissed() {
        //广告加载完成
        startMainActivity();
    }

    @Override
    public void onNoAD(AdError adError) {
        // 如果加载广告失败，则直接跳转
        startMainActivity();
    }

    @Override
    public void onADPresent() {
        //广告被正常弹出
        ImageView log = (ImageView) findViewById(R.id.splash_holder);
        log.setVisibility(View.GONE);
        TextView textTip = (TextView) findViewById(R.id.welcome_bottom_tip);
        textTip.setVisibility(View.VISIBLE);
    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADTick(long l) {
        //显示倒计时
    }
}
