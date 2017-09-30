package ywcai.ls.mobileutil.welcome.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.presenter.ActivityRouter;
import ywcai.ls.mobileutil.global.presenter.inf.RouterInf;

public class WelComeActivity extends AppCompatActivity {
    RouterInf router;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.welcome_activity);
        router=new ActivityRouter();
        router.startActivity(this,"ywcai.ls.mobileutil.main.view.MainActivity");
    }
    @Override
    public void onBackPressed() {

    }
}
