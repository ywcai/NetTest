package ywcai.ls.mobileutil.identity.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.identity.Presenter.Login;

public class LoginActivity extends AppCompatActivity {
    private Login action = new Login();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SetTitle.setTitleTransparent(getWindow());
        setContentView(R.layout.activity_login);
        InitToolBar();
        InitActionEvent();
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
        setSupportActionBar(mToolbar);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu1:
//                        break;
//                    case R.id.menu2:
//                        break;
//                    case R.id.menu3:
//                        break;
//                    case R.id.menu4:
//                        break;
//                }
//                return false;
//            }
//        });
    }

    private void InitActionEvent()
    {
        final AppCompatButton loginButton = (AppCompatButton) findViewById(R.id.login_login_action);
        final TextInputLayout username = (TextInputLayout) findViewById(R.id.login_input_username);
        final TextInputLayout password = (TextInputLayout) findViewById(R.id.login_input_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = username.getEditText().getText().toString();
                String psw = password.getEditText().getText().toString();
                loginButton.setEnabled(false);
                action.loginForSelf(userId, psw);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_detail_local, menu);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return true;
    }

    @Override
    public void onBackPressed() {

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

    private void showInfo(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLoginView(GlobalEvent event) {
//        switch (event.type) {
//            case LoginEventT.local_input_err:
//                showInfo("用户名密码格式填写错误!");
//                break;
//            case LoginEventT.server_pass:
//                showInfo("账号验证通过!");
//                router.createNewSession();
//                break;
//            case LoginEventT.server_refuse:
//                showInfo("服务器拒绝:"+event.tip);
//                break;
//            case LoginEventT.net_err:
//                showInfo("网络错误");
//                break;
//        }
    }

}
