package ywcai.ls.mobileutil.tools.ScanBle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;


import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;


@Route(path = "/tools/ScanBle/BleActivity")
public class BleActivity extends AppCompatActivity {
    private TextView textView;
    private BluetoothLeScanner mScanner;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_ble);
        InitToolBar();
        textView = (TextView) findViewById(R.id.ble_text);
//        mScanner=new BluetoothLeScanner(mLeScanCallback,null);
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.ble_toolbar);
        mToolbar.setTitleMarginStart(0);
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
        return false;
    }


}
