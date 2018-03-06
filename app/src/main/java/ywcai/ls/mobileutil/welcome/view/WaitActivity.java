package ywcai.ls.mobileutil.welcome.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import com.alibaba.android.arouter.facade.annotation.Route;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.util.statics.LsToolbar;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;


@Route(path = "/welcome/view/WaitActivity")
public class WaitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_wait);
        InitToolBar();
    }


    private void InitToolBar() {
        LsToolbar.initToolbar(this, "开发中");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
