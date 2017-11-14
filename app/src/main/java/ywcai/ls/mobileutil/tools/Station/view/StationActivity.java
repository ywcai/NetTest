package ywcai.ls.mobileutil.tools.Station.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.Station.presenter.StationAction;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationActionInf;

@Route(path = "/tools/Station/view/StationActivity")
public class StationActivity extends AppCompatActivity {

    private StationActionInf action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_station);
        InitToolBar();
        InitView();
        RegListener();
    }

    private void InitView() {
        TextView textView=(TextView)findViewById(R.id.station_text);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        TextView text2 = (TextView) findViewById(R.id.station_text2);
        text2.setMovementMethod(ScrollingMovementMethod.getInstance());
        TextView text3 = (TextView) findViewById(R.id.station_text3);
        text3.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void RegListener() {
        action = new StationAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.station_toolbar);
        mToolbar.setTitleMarginStart(0);
        mToolbar.setTitle("基站信号");
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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.station_set_entry_change:
                showText(event.tip);
                break;
            case GlobalEventT.station_set_cell_change:
                showText2(event.tip);
                break;
            case GlobalEventT.station_set_cell_change1:
                showText3(event.tip);
                break;
        }
    }

    private void showText(String tip) {
        TextView text = (TextView) findViewById(R.id.station_text);
        text.setText(tip);
    }
    private void showText2(String tip) {
        TextView text = (TextView) findViewById(R.id.station_text2);
        text.setText(tip);
    }
    private void showText3(String tip) {
        TextView text = (TextView) findViewById(R.id.station_text2);
        text.setText(tip);
    }

}
