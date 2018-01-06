package ywcai.ls.mobileutil.tools.Station.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mobstat.StatService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


import rx.schedulers.Schedulers;
import ywcai.ls.control.LoadingDialog;
import ywcai.ls.control.flex.FlexButtonLayout;
import ywcai.ls.control.flex.OnFlexButtonClickListener;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;
import ywcai.ls.mobileutil.tools.Station.presenter.MainStationAction;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.MainStationActionInf;

@Route(path = "/tools/Station/view/StationActivity")
public class StationActivity extends AppCompatActivity {
    private LineChart stationInfoRecord;
    private MainStationActionInf mainStationActionInf;
    private MaterialDialog popMenu;
    private int baseMaxX = 50;
    private String cellLog = "", signalLog = "";
    private FlexButtonLayout topBtn;
    private LoadingDialog loadingDialog;

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
        loadingDialog = new LoadingDialog(this);
        popMenu = new MaterialDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_dialog_wifi, null);
        popMenu.setContentView(view);
        popMenu.setCanceledOnTouchOutside(false);
        Button save_local = (Button) view.findViewById(R.id.wifi_save_local);
        Button save_remote = (Button) view.findViewById(R.id.wifi_save_remote);
        Button clear = (Button) view.findViewById(R.id.wifi_clear);
        Button cancal = (Button) view.findViewById(R.id.wifi_cancal);
        clear.setText("重置数据");
        save_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu(false);
                popLoading(AppConfig.LOG_PROCESS_TIP);
                mainStationActionInf.saveLogLocal();
            }
        });
        save_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu(false);
                mainStationActionInf.saveLogRemote();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu(false);
                mainStationActionInf.clearTask();
            }
        });
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu(false);
            }
        });

        topBtn = (FlexButtonLayout) findViewById(R.id.station_top_btn);
        List list = new ArrayList();
        list.add("图表");
        list.add("摘要");
        topBtn.setDataAdapter(list);
        topBtn.setOnFlexButtonClickListener(new OnFlexButtonClickListener() {
            @Override
            public void clickItem(int i, boolean b) {
                mainStationActionInf.selectFlexButton(i);
            }

            @Override
            public void clickAllBtn(int[] ints, boolean b) {

            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.station_btn_show_menu);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu(true);
            }
        });
    }

    private void createLineChart() {
        stationInfoRecord = (LineChart) findViewById(R.id.station_task_record);
        stationInfoRecord.setTouchEnabled(false);
        stationInfoRecord.getAxisRight().setEnabled(false);
        stationInfoRecord.setDrawGridBackground(false);
        stationInfoRecord.setTouchEnabled(false);
        stationInfoRecord.setDragEnabled(false);
        stationInfoRecord.setScaleEnabled(false);
        stationInfoRecord.setDoubleTapToZoomEnabled(false);
        stationInfoRecord.setHighlightPerTapEnabled(false);
        stationInfoRecord.setHighlightPerDragEnabled(false);
        YAxis leftAxis = stationInfoRecord.getAxisLeft();
        leftAxis.setAxisMaximum(-40);
        leftAxis.setAxisMinimum(-160);
        leftAxis.setTextSize(6);
        leftAxis.setTextColor(ContextCompat.getColor(this, R.color.LBlue));
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
        stationInfoRecord.setNoDataText("检测信号变化后，系统会自动重绘折线");
        refreshChartData(null);

    }

    private void refreshChartData(List<Entry> entryList) {

        stationInfoRecord.setData(null);
        if (entryList == null) {
            stationInfoRecord.invalidate();
            return;
        }
        if (entryList.size() <= 0) {
            stationInfoRecord.invalidate();
            return;
        }
        List<ILineDataSet> listDataSets = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entryList, "");//图列名称
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setHighlightEnabled(false);
        dataSet.setColor(ContextCompat.getColor(this, R.color.LOrange));
        dataSet.setLineWidth(2f);
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
                mainStationActionInf = new MainStationAction(StationActivity.this, StationActivity.this);
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
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
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
    protected void onDestroy() {
        //非常重要，否则会造成下一次开启后监听数据混乱
        mainStationActionInf.unRegPhoneStateListener();
        super.onDestroy();
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
                showCellLog((HashMap<String, Integer>) event.obj);
                break;
            case GlobalEventT.station_refresh_signal_log_info:
                showSignalLog((HashMap<String, Integer>) event.obj);
                break;
            case GlobalEventT.station_set_toolbar_center_text:
                showTopTip(event.tip);
                break;
            case GlobalEventT.station_refresh_chart_entry_record:
                refreshChartData((List<Entry>) event.obj);
                break;
            case GlobalEventT.global_pop_operator_dialog:
                popMenu((boolean) event.obj);
                break;
            case GlobalEventT.global_pop_snack_tip:
                closeLoading();
                showSnackTip(event.tip, (boolean) event.obj);
                break;
            case GlobalEventT.station_switch_top_btn:
                switchShowContent((boolean) event.obj);
                break;
        }
    }

    private void closeLoading() {
        loadingDialog.dismiss();
    }

    private void popLoading(String tip) {
        loadingDialog.setLoadingText(tip);
        loadingDialog.show();
    }


    private void popMenu(boolean obj) {
        if (obj) {
            popMenu.show();
        } else {
            popMenu.dismiss();
        }
    }

    private void showTopTip(String tip) {
        TextView topTip = (TextView) findViewById(R.id.station_toolbar_tip);
        topTip.setText(tip);
    }

    private void switchShowContent(boolean isShowFormat) {
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.station_btn_show_menu);
        CardView card1 = (CardView) findViewById(R.id.station_main_chart_container);
        CardView card2 = (CardView) findViewById(R.id.station_detail_card);
        if (isShowFormat) {
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.INVISIBLE);
            topBtn.setCurrentIndex(0);
            topBtn.getChildAt(0).setClickable(false);
            topBtn.getChildAt(1).setClickable(true);
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            card1.setVisibility(View.INVISIBLE);
            card2.setVisibility(View.VISIBLE);
            topBtn.setCurrentIndex(1);
            topBtn.getChildAt(0).setClickable(true);
            topBtn.getChildAt(1).setClickable(false);
            floatingActionButton.setVisibility(View.INVISIBLE);
        }


    }

    private void showSnackTip(String tip, boolean isSuccess) {
        RelativeLayout snack_container = (RelativeLayout) findViewById(R.id.station_snack_container);
        if (isSuccess) {
            LsSnack.show(this, snack_container, tip);
        } else {
            LsSnack.show(this, snack_container, tip, R.color.LRed);
        }
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

    private void showSignalLog(HashMap<String, Integer> obj) {
        signalLog = "";
        TextView logText = (TextView) findViewById(R.id.station_detail_text);
        logText.setMovementMethod(new ScrollingMovementMethod());
        Iterator it = obj.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = entry.getKey() + "";
            String val = entry.getValue() + "";
            if (key.contains("Gsm")
                    || key.contains("Wcdma")
                    || key.contains("Lte")
                    || key.contains("Cdma")
                    ) {
                signalLog += key + " : [" + val + "]\n";
            }
        }
        String temp = "-----小区原始数据-----\n" + cellLog + "\n----信号强度数据----\n" + signalLog;
        logText.setText(temp);
    }

    private void showCellLog(HashMap<String, Integer> obj) {
        cellLog = "";
        TextView logText = (TextView) findViewById(R.id.station_detail_text);
        logText.setMovementMethod(new ScrollingMovementMethod());
        Iterator it = obj.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = entry.getKey() + "";
            String val = entry.getValue() + "";
            cellLog += key + " : [" + val + "]\n";
        }
        String temp = "-----小区原始数据-----\n" + cellLog + "\n----信号强度数据----\n" + signalLog;
        logText.setText(temp);
    }


}
