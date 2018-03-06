package ywcai.ls.mobileutil.tools.Ble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.util.statics.LsToolbar;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;


@Route(path = "/tools/Ble/BleActivity")
public class BleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_ble);
        InitToolBar();
    }

    private void InitToolBar() {
        LsToolbar.initToolbar(this, AppConfig.TITLE_BLE);
    }
}
