package ywcai.ls.mobileutil.tools.Station.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ywcai.ls.control.flex.FlexButtonLayout;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;
import ywcai.ls.mobileutil.tools.Station.presenter.MainStationAction;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.MainStationActionInf;

@Route(path = "/tools/Station/view/StationActivity")
public class StationActivity extends AppCompatActivity {
    private LineChart stationInfoRecord;
    private MainStationActionInf mainStationActionInf;
    private int baseMaxX = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_station);
        InitToolBar();
        InitView();
        InitAction();
        createLineChart();
    }

    private void InitView() {
        FlexButtonLayout flexBtn = (FlexButtonLayout) findViewById(R.id.station_top_btn);
        List list = new ArrayList();
        list.add("摘要");
        list.add("详细");
        flexBtn.setDataAdapter(list);
        flexBtn.setCurrentIndex(0);
    }

    private void createLineChart() {
        stationInfoRecord = (LineChart) findViewById(R.id.station_task_record);
        stationInfoRecord.getAxisRight().setEnabled(false);
        stationInfoRecord.setDrawGridBackground(false);
        stationInfoRecord.setNoDataText("");
        stationInfoRecord.setTouchEnabled(true);
        stationInfoRecord.setDragEnabled(true);
        stationInfoRecord.setScaleEnabled(true);
        stationInfoRecord.setDoubleTapToZoomEnabled(false);
        stationInfoRecord.setHighlightPerTapEnabled(false);
        stationInfoRecord.setHighlightPerDragEnabled(false);
        YAxis leftAxis = stationInfoRecord.getAxisLeft();
        leftAxis.setAxisMaximum(-40);
        leftAxis.setAxisMinimum(-160);
        leftAxis.setTextSize(10);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(true);
        leftAxis.enableGridDashedLine(10, 20f, 0);
        leftAxis.setDrawGridLines(true);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value <= -160) {
                    return "无信号";
                }
                return value + "";
            }
        });
        XAxis xAxis = stationInfoRecord.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.setAxisMaximum(baseMaxX);
        xAxis.setAxisMinimum(0);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });
        stationInfoRecord.setDescription(null);
        stationInfoRecord.getLegend().setEnabled(false);
        stationInfoRecord.setAutoScaleMinMaxEnabled(true);
        stationInfoRecord.setNoDataText("点击右下角按钮可添加数据记录任务");
        refreshChartData(null);

    }

    private void refreshChartData(List<Entry> entryList) {
        if (stationInfoRecord.getLineData() != null) {
            stationInfoRecord.setData(null);
        }
        if (entryList == null) {
            entryList = new ArrayList<>();
            entryList.add(new Entry(0, -40));
        }
        List<ILineDataSet> listDataSets = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entryList, "");//图列名称
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setHighlightEnabled(false);
        dataSet.setColor(ContextCompat.getColor(this, AppConfig.colors[0]));
        dataSet.setLineWidth(1f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        listDataSets.add(dataSet);
        LineData lineData = new LineData(listDataSets);
        stationInfoRecord.setData(lineData);
        stationInfoRecord.invalidate();
    }

    private void InitAction() {
        Observable.create(new Observable.OnSubscribe<MainStationActionInf>() {
            @Override
            public void call(Subscriber<? super MainStationActionInf> subscriber) {
                mainStationActionInf = new MainStationAction(StationActivity.this);
                subscriber.onNext(mainStationActionInf);
            }
        })
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MainStationActionInf>() {
                    @Override
                    public void call(MainStationActionInf mainStationActionInf) {
                        mainStationActionInf.startWork();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.station_toolbar);
        mToolbar.setTitleMarginStart(0);
        TextView topTitle = (TextView) findViewById(R.id.station_toolbar_title);
        topTitle.setText(AppConfig.TITLE_STATION);
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
            case GlobalEventT.station_refresh_entry_info:
                refreshStationInfo((StationEntry) event.obj);
                break;
            case GlobalEventT.station_refresh_cell_log_info:
//                showCellLog((HashMap<String, Integer>) event.obj);
                break;
            case GlobalEventT.station_refresh_signal_log_info:
//                showSignalLog((HashMap<String, Integer>) event.obj);
                break;
            case GlobalEventT.station_set_toolbar_center_text:
                showTopTip(event.tip);
                break;
            case GlobalEventT.station_refresh_chart_entry_record:
                refreshChartData((List<Entry>) event.obj);
                break;
        }
    }

    private void showTopTip(String tip) {
        TextView topTip = (TextView) findViewById(R.id.station_toolbar_tip);
        topTip.setText(tip);
    }

    private void refreshStationInfo(StationEntry stationEntry) {
        TextView imei = (TextView) findViewById(R.id.station_main_base_imei);
        TextView cardNumber = (TextView) findViewById(R.id.station_main_base_card_num);
        TextView lac = (TextView) findViewById(R.id.station_main_entry_lac);
        TextView cid = (TextView) findViewById(R.id.station_main_entry_cid);
        TextView db = (TextView) findViewById(R.id.station_main_entry_dbm);
        imei.setText(stationEntry.imei);
        cardNumber.setText(stationEntry.cardNumber);
        lac.setText(stationEntry.lac + "");
        cid.setText(stationEntry.cid + "");
        db.setText(stationEntry.rsp + " dbm");
    }




        //在Model里处理数据;
//        Observable.from(list)
//                .map(new Func1<StationEntry, Entry>() {
//                    @Override
//                    public Entry call(StationEntry stationEntry) {
//                        if (stationEntry.rsp >= -1) {
//                            return new Entry(list.indexOf(stationEntry), -160);
//                        }
//                        return new Entry(list.indexOf(stationEntry), stationEntry.rsp);
//                    }
//                })
//                .toList()
//                .subscribe(new Action1<List<Entry>>() {
//                    @Override
//                    public void call(List<Entry> entries) {
//                        refreshChartData(entries);
//                    }
//                });

//    private void showSignalLog(HashMap<String, Integer> obj) {
//        String s = "";
//        Iterator it = obj.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry entry = (Map.Entry) it.next();
//            Object key = entry.getKey();
//            Object val = entry.getValue();
//            s += (String) key + ":" + (Integer) val + " ";
//        }
//        TextView text = (TextView) findViewById(R.id.station_text2);
//        text.setText(s);
//    }
//
//    private void showCellLog(HashMap<String, Integer> obj) {
//        String s = "";
//        Iterator it = obj.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry entry = (Map.Entry) it.next();
//            Object key = entry.getKey();
//            Object val = entry.getValue();
//            s += (String) key + ":" + (Integer) val + "\n ";
//        }
//        TextView text = (TextView) findViewById(R.id.station_text3);
//        text.setText(s);
//    }


}
