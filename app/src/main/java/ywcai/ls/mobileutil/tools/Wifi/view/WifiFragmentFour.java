package ywcai.ls.mobileutil.tools.Wifi.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import mehdi.sakout.fancybuttons.FancyButton;
import rx.Observable;
import rx.functions.Action1;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;


public class WifiFragmentFour extends Fragment {
    private View view;
    private MainWifiActionInf mainWifiActionInf;
    private LineChart wifiChannelRecord;
    private int baseMaxX = 100;
    private FancyButton btnSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_wifi_four, container, false);
        createLineChart();
        initBtn();
        return view;
    }

    private void initBtn() {
        btnSave = (FancyButton) view.findViewById(R.id.wifi_record_bitmap_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //三秒类只允许点击一次。
                Observable
                        .just("")
                        .throttleFirst(5, TimeUnit.SECONDS)
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                mainWifiActionInf.saveBitmap(wifiChannelRecord);
                            }
                        });
            }
        });
    }

    public void setGlobalAction(MainWifiActionInf _mainWifiActionInf) {
        mainWifiActionInf = _mainWifiActionInf;
    }

    private void createLineChart() {
        wifiChannelRecord = (LineChart) view.findViewById(R.id.wifi_channel_record_chart);
        wifiChannelRecord.getAxisRight().setEnabled(false);
        wifiChannelRecord.setDrawGridBackground(false);
        wifiChannelRecord.setNoDataText("");
        wifiChannelRecord.setTouchEnabled(true);
        wifiChannelRecord.setDragEnabled(true);
        wifiChannelRecord.setScaleEnabled(true);
        wifiChannelRecord.setDoubleTapToZoomEnabled(false);
        wifiChannelRecord.setHighlightPerTapEnabled(false);
        wifiChannelRecord.setHighlightPerDragEnabled(false);
        YAxis leftAxis = wifiChannelRecord.getAxisLeft();
        leftAxis.setAxisMaximum(10);
        leftAxis.setAxisMinimum(0);
        leftAxis.setLabelCount(5, false);
        leftAxis.setTextSize(10);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        leftAxis.enableGridDashedLine(10, 20f, 0);
        leftAxis.setDrawGridLines(false);
        XAxis xAxis = wifiChannelRecord.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.setAxisMaximum(baseMaxX);
        xAxis.setLabelCount(10, false);
        xAxis.setAxisMinimum(0);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });
        Description des = new Description();
        des.setText("");
        wifiChannelRecord.setDescription(des);
        wifiChannelRecord.getLegend().setWordWrapEnabled(true);
        wifiChannelRecord.getLegend().setTextSize(12);
        wifiChannelRecord.getLegend().setTextColor(Color.WHITE);
        wifiChannelRecord.setAutoScaleMinMaxEnabled(true);
    }

    private void createNewLineData(int[] allChannelNum) {
        List<ILineDataSet> listDataSets = new ArrayList<>();
        for (int i = 0; i < AppConfig.INTS_CHANNEL_2D4G.length; i++) {
            List<Entry> chartData = new ArrayList<>();
            chartData.add(new Entry(0, allChannelNum[AppConfig.INTS_CHANNEL_2D4G[i]]));
            LineDataSet dataSet = new LineDataSet(chartData, "CH" + AppConfig.INTS_CHANNEL_2D4G[i]);//图列名称
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
            dataSet.setHighlightEnabled(false);
            dataSet.setColor(ContextCompat.getColor(getActivity(), AppConfig.colors[i]));
            dataSet.setLineWidth(1.5f);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            listDataSets.add(dataSet);
        }
        for (int i = 0; i < AppConfig.INTS_CHANNEL_5G.length; i++) {
            List<Entry> chartData = new ArrayList<>();
            chartData.add(new Entry(0, allChannelNum[AppConfig.INTS_CHANNEL_5G[i]]));
            LineDataSet dataSet = new LineDataSet(chartData, "CH" + AppConfig.INTS_CHANNEL_5G[i]);//图列名称
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
            dataSet.setHighlightEnabled(false);
            dataSet.setColor(ContextCompat.getColor(getActivity(), AppConfig.colors2[i]));
            dataSet.setLineWidth(1.5f);
            listDataSets.add(dataSet);
        }
        LineData lineData = new LineData(listDataSets);
        wifiChannelRecord.setData(lineData);
        wifiChannelRecord.invalidate();
    }

    private void drawLine(int[] allChannelNum) {
        if (wifiChannelRecord.getLineData() == null) {
            createNewLineData(allChannelNum);
            return;
        }
        List<ILineDataSet> list = wifiChannelRecord.getLineData().getDataSets();
        for (int i = 0; i < list.size(); i++) {
            int size = list.get(i).getEntryCount();
            if (size >= wifiChannelRecord.getXChartMax() - 10) {
                wifiChannelRecord.getXAxis().setAxisMaximum(size + baseMaxX);
            }
            for (int n = 0; n < size; n++) {
                float xPos = list.get(i).getEntryForIndex(n).getX();
                list.get(i).getEntryForIndex(n).setX(xPos + 1);
            }
            Entry entry;
            if (i < 13) {
                entry = new Entry(0, allChannelNum[AppConfig.INTS_CHANNEL_2D4G[i]]);

            } else {
                entry = new Entry(0, allChannelNum[AppConfig.INTS_CHANNEL_5G[i - 13]]);
            }
            list.get(i).addEntryOrdered(entry);
            if (entry.getY() >= wifiChannelRecord.getYChartMax() - 1) {
                wifiChannelRecord.getAxisLeft().setAxisMaximum(entry.getY() + 5);

            }
        }
        wifiChannelRecord.invalidate();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void update(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.wifi_refresh_three_line:
                drawLine((int[]) event.obj);
                break;
            case GlobalEventT.wifi_switch_2d4g:
                switchLineChoose((boolean) event.obj);
                break;
        }
    }

    //切换频率变换时显示的折线
    private void switchLineChoose(boolean isChoose2d4G) {
        List<ILineDataSet> list = wifiChannelRecord.getLineData().getDataSets();
        for (int i = 0; i < list.size(); i++) {
            if (isChoose2d4G && i < 13) {
                wifiChannelRecord.getLineData().getDataSetByIndex(i).setVisible(true);
            } else if ((!isChoose2d4G) && (i >= 13)) {
                wifiChannelRecord.getLineData().getDataSetByIndex(i).setVisible(true);
            } else {
                wifiChannelRecord.getLineData().getDataSetByIndex(i).setVisible(false);
            }
        }
        wifiChannelRecord.invalidate();
    }

}
