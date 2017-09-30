package ywcai.ls.mobileutil.tools.Ping.view;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.DrawEventT;

public class PingActivity extends AppCompatActivity {
    private LineChart pingResultChart;
    public LineData lineData = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
        setContentView(R.layout.activity_ping);
        InitToolBar();
        InitChart();
        InitActionEvent();
    }

    private void InitChart() {
        pingResultChart=(LineChart)findViewById(R.id.pingResult);
        lineData = getLineData();
        pingResultChart.setData(lineData);
        showChart();
    }
    public void showChart() {
        pingResultChart.setDrawGridBackground(false);
        pingResultChart.setScaleEnabled(false);
        pingResultChart.setTouchEnabled(false);
        pingResultChart.setContentDescription("");
        LimitLine ll1 = new LimitLine(200, "Slow");
        ll1.setLineWidth(1);
        ll1.setTextSize(8);
        ll1.setLineColor(Color.RED);
        LimitLine ll2 = new LimitLine(50, "Normal");
        ll2.setLineWidth(1);
        ll2.setTextSize(8);
        ll2.setLineColor(Color.GREEN);

        YAxis leftAxis = pingResultChart.getAxisLeft();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(500);
        leftAxis.setAxisMinimum(0);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        XAxis xAxis = pingResultChart.getXAxis();
        xAxis.setAxisMaximum(500);
        xAxis.removeAllLimitLines();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        pingResultChart.getAxisRight().setEnabled(false);
    }

    private LineData getLineData(){
        final int DATA_COUNT = 500;  //資料數固定為 5 筆
        LineDataSet dataSetB = new LineDataSet( getChartData(DATA_COUNT, 2), "119.6.6.6");
        dataSetB.setDrawCircles(false);
        dataSetB.setColor(Color.MAGENTA);
        dataSetB.setLineWidth(1);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetB);
        LineData data = new LineData(dataSets);
        return data;
    }

    private List<Entry> getChartData(int count, int ratio){

        List<Entry> chartData = new ArrayList<>();
        for(int i=0;i<count;i++){
                chartData.add(new Entry(i,0));
        }
        return chartData;
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
        Toolbar mToolbar = (Toolbar) findViewById(R.id.ping_toolbar);
        mToolbar.setTitle("Ping Test");
        setSupportActionBar(mToolbar);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    private void InitActionEvent()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.home){
//            return true;
//        }
        Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
        PingActivity.this.finish();
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLoginView(GlobalEvent event) {
        switch (event.type)
        {
            case DrawEventT.draw_ping:
                LineData lineData=pingResultChart.getLineData();
                ILineDataSet iLineDataSet=lineData.getDataSetByIndex(0);
                Entry entry=iLineDataSet.getEntryForIndex((int)event.obj);
                entry.setY((float) Math.random()*500);
                pingResultChart.invalidate();
                break;
        }
    }
}
