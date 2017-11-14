package ywcai.ls.mobileutil.welcome.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.alibaba.android.arouter.launcher.ARouter;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;


public class WelComeActivity extends AppCompatActivity {
    public String ROUTER_PAGE = "First";

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
        //通知栏打开程序，又有两个情况。
//        if (isBroughtToFront) {
            //打开了一个已存在的任务，并且这个栈被移到了栈顶。则直接路由到MAIN界面，并传递MAIN的路由参数
            //不存在实例，
            if (!MainApplication.getInstance().isActivityExist) {
                /*
                重建时，重绘welcome页面后再进入，否则会闪白屏，不是很美观
                 */
                this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
                setContentView(R.layout.activity_welcome);
                MainApplication.getInstance().isActivityExist = true;
                /*
                路由信息，可根据上一次应用关闭时的TOP activity缓存来恢复
                 */
                startMainActivity();
            } else {
                //已存在，直接关闭这次吊起的页面即可
                finish();
            }
            return;
//        }
        //第一次启动，打开欢迎的启动界面
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
//        setContentView(R.layout.activity_welcome);
//        startMainActivity();
//        //标识应用已经建立了ACTIVITY
//        MainApplication.getInstance().isActivityExist = true;
    }

    @Override
    public void onBackPressed() {

    }

    private void startMainActivity()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ARouter.getInstance().
                        build(AppConfig.MAIN_ACTIVITY_PATH).withString(AppConfig.ROUTER_FLAG, ROUTER_PAGE).
                        navigation();
            }
        }).start();
    }

}
