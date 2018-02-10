package ywcai.ls.mobileutil.login.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.concurrent.TimeUnit;
import rx.Observable;


import rx.functions.Action1;
import ywcai.ls.control.LoadingDialog;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.ObservableTransformer;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.login.Presenter.Login;
import ywcai.ls.mobileutil.login.Presenter.inf.LoginInf;
import ywcai.ls.mobileutil.login.model.MyUser;
import ywcai.ls.mobileutil.login.model.QQBackResult;


@Route(path = "/login/view/LoginActivity")
public class LoginActivity extends AppCompatActivity {
    private LoginInf loginInf = new Login();
    private IUiListener qqListener = new QQListener();
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());
        setContentView(R.layout.activity_login);
        loadingDialog = new LoadingDialog(this);
        InitToolBar();
        InitAction();
    }

    private void InitAction() {
        AppCompatImageView loginMM = (AppCompatImageView) findViewById(R.id.login_mm);
        AppCompatImageView loginQQ = (AppCompatImageView) findViewById(R.id.login_qq);
        loginMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loginQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                loginInf.loginForQQ(LoginActivity.this, qqListener);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class QQListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            String json = o.toString();
            Gson gson = new Gson();
            QQBackResult qqResult = gson.fromJson(json, QQBackResult.class);
            loadingDialog.setLoadingText("正在验证用户信息...");
            HttpService service = RetrofitFactory.getHttpService();
            Observable observable = service.login(qqResult.openid, AppConfig.LOGIN_QQ_CHANNEL);
            observable.compose(ObservableTransformer.schedulersTransformer()).subscribe(
                    new BaseObserver<MyUser>() {
                        @Override
                        protected void success(MyUser myUser) {
                            loginSuccess(myUser);
                        }

                        @Override
                        protected void onCodeError(int code, String msg) {
                            loginFail(code, msg);
                        }

                        @Override
                        protected void onNetError(Throwable e) {
                            connectFail(e.toString());
                        }
                    }
            );
        }

        @Override
        public void onError(UiError uiError) {
            loadingDialog.dismiss();
            showInfo("登录失败:" + uiError.toString());
        }

        @Override
        public void onCancel() {
            loadingDialog.dismiss();
            showInfo("取消登录");
        }
    }

    private void connectFail(String s) {
        //提示网络连接失败
        loadingDialog.setLoadingText("loading...");
        loadingDialog.dismiss();
        showInfo("网络连接异常 " + s);
    }

    private void loginFail(int code, String msg) {
        //提示服务端错误代码
        loadingDialog.setLoadingText("loading...");
        loadingDialog.dismiss();
        showInfo("认证被拒绝 {code:" + code + "  msg:" + msg + "}");
    }

    private void loginSuccess(MyUser myUser) {
        //保存缓存状态后关闭
        CacheProcess cacheProcess = CacheProcess.getInstance();
        cacheProcess.setCacheUser(myUser);
        loadingDialog.setLoadingText("登录成功，即将退出登录页面!");
        MsgHelper.sendStickEvent(GlobalEventT.setting_qq_login_success, "", myUser);
        Observable.just("").delay(3, TimeUnit.SECONDS)
                .compose(ObservableTransformer.schedulersTransformer())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LoginActivity.this.finish();
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

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        mToolbar.setTitle("");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_detail_local, menu);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return false;
    }

    private void showInfo(String tip) {
        RelativeLayout snack_container = (RelativeLayout) findViewById(R.id.login_snack_container);
        LsSnack.show(this, snack_container, tip, R.color.LRed);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateLoginView(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.test_tip:
                showInfo(event.tip);
                break;
        }
    }

}
