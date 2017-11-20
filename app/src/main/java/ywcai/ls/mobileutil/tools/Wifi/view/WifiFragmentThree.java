package ywcai.ls.mobileutil.tools.Wifi.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;
import ywcai.ls.mobileutil.service.LsPendingIntent;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;
import ywcai.ls.mobileutil.tools.Wifi.model.two.WifiTaskAdapter;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;

import static android.content.Context.NOTIFICATION_SERVICE;


public class WifiFragmentThree extends Fragment {
    private View view;
    private LineChart wifiChannelRecord;
    private BarChart wifiChannelCurrent;
    private PieChart wifiChannel2d4g, wifiChannel5g;
    private MainWifiActionInf mainWifiActionInf;
    private int baseMaxX = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_wifi_three, container, false);
        createLineChart();
        createBarChart();
        createPieChart1();
        createPieChart2();
        return view;
    }


    public void setGlobalAction(MainWifiActionInf _mainWifiActionInf) {
        mainWifiActionInf = _mainWifiActionInf;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.wifi_refresh_three_bar:
                recoveryBarChartBaseData(event.tip, (int[]) event.obj);
                break;
            case GlobalEventT.wifi_refresh_three_pie:
                recoveryPie1ChartBaseData((int[]) event.obj);
                recoveryPie2ChartBaseData((int[]) event.obj);
                break;
        }
    }

    private void createLineChart() {
        wifiChannelRecord = (LineChart) view.findViewById(R.id.wifi_channel_record_chart);
        wifiChannelRecord.getAxisRight().setEnabled(false);
        wifiChannelRecord.setDrawGridBackground(false);
        wifiChannelRecord.setNoDataText("暂时没有启动任务，记录信道占用情况");
        wifiChannelRecord.setTouchEnabled(true);
        wifiChannelRecord.setDragEnabled(true);
        wifiChannelRecord.setScaleEnabled(true);
        wifiChannelRecord.setDoubleTapToZoomEnabled(false);
        wifiChannelRecord.setHighlightPerTapEnabled(false);
        wifiChannelRecord.setHighlightPerDragEnabled(false);
        YAxis leftAxis = wifiChannelRecord.getAxisLeft();
        leftAxis.setAxisMaximum(20);
        leftAxis.setAxisMinimum(0);
        leftAxis.setLabelCount(5, false);
        leftAxis.setTextSize(10);
        leftAxis.enableGridDashedLine(10, 20f, 0);
        XAxis xAxis = wifiChannelRecord.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.setAxisMaximum(baseMaxX);
        xAxis.setLabelCount(10, false);
        xAxis.setAxisMinimum(0);
        xAxis.setDrawGridLines(false);
        Description des = new Description();
        des.setText("");
        wifiChannelRecord.setDescription(des);
        wifiChannelRecord.getLegend().setWordWrapEnabled(true);
        wifiChannelRecord.getLegend().setTextSize(6);
        recoveryLineChartBaseData();
    }

    private void recoveryLineChartBaseData() {

    }

    private void createBarChart() {
        wifiChannelCurrent = (BarChart) view.findViewById(R.id.wifi_channel_count_current_chart);
        wifiChannelCurrent.getLegend().setEnabled(false);
        wifiChannelCurrent.getXAxis().setEnabled(true);
        wifiChannelCurrent.getXAxis().setDrawGridLines(false);//不显示网格
        wifiChannelCurrent.getXAxis().removeAllLimitLines();
        wifiChannelCurrent.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        wifiChannelCurrent.getXAxis().setAxisMinimum(-0.5f);
        wifiChannelCurrent.getXAxis().setAxisMaximum(12.5f);
        wifiChannelCurrent.getXAxis().setAxisLineColor(ContextCompat.getColor(this.getActivity(), R.color.colorWifiBg));
        wifiChannelCurrent.getXAxis().setTextColor(0xFFFFFFFF);
        wifiChannelCurrent.getXAxis().setTextSize(10);
        wifiChannelCurrent.setTouchEnabled(false);
        wifiChannelCurrent.getAxisLeft().setDrawGridLines(false);//不设置Y轴网格
        wifiChannelCurrent.getAxisLeft().setEnabled(false);//左侧不显示Y轴
        wifiChannelCurrent.getAxisRight().setEnabled(false);//右侧不显示Y轴
        wifiChannelCurrent.getAxisLeft().setAxisMinimum(0);
        wifiChannelCurrent.getAxisLeft().setAxisMaximum(10);
        wifiChannelCurrent.getAxisLeft().setTextSize(10);
        wifiChannelCurrent.setDescription(null);

    }

    private void createPieChart1() {
        wifiChannel2d4g = (PieChart) view.findViewById(R.id.wifi_three_pie_left);
        wifiChannel2d4g.setTouchEnabled(false);
        wifiChannel2d4g.getLegend().setEnabled(false);
        wifiChannel2d4g.setDescription(null);
        wifiChannel2d4g.setUsePercentValues(false);
        wifiChannel2d4g.setHoleRadius(60);
        wifiChannel2d4g.setTransparentCircleRadius(63);
        wifiChannel2d4g.setDrawHoleEnabled(true);
        wifiChannel2d4g.setHoleColor(Color.TRANSPARENT);
        wifiChannel2d4g.setEntryLabelTextSize(10);
        wifiChannel2d4g.setTransparentCircleColor(ContextCompat.getColor(this.getActivity(), R.color.colorWifiBg));
        wifiChannel2d4g.setTransparentCircleAlpha(0);
    }

    private void createPieChart2() {
        wifiChannel5g = (PieChart) view.findViewById(R.id.wifi_three_pie_right);
        wifiChannel5g.setTouchEnabled(false);
        wifiChannel5g.getLegend().setEnabled(false);
        wifiChannel5g.setDescription(null);
        wifiChannel5g.setUsePercentValues(false);
        wifiChannel5g.setHoleRadius(60);
        wifiChannel5g.setTransparentCircleRadius(63);
        wifiChannel5g.setDrawHoleEnabled(true);
        wifiChannel5g.setHoleColor(Color.TRANSPARENT);
        wifiChannel5g.setEntryLabelTextSize(10);
        wifiChannel5g.setTransparentCircleColor(ContextCompat.getColor(this.getActivity(), R.color.colorWifiBg));
        wifiChannel5g.setTransparentCircleAlpha(0);

    }

    private void recoveryPie1ChartBaseData(int channelSum[]) {
        if (wifiChannel2d4g.getData() != null) {
            wifiChannel2d4g.setData(null);
        }
//        List<String> x = new ArrayList<String>();
        List<PieEntry> y = new ArrayList<>();
        for (int i = 0; i < AppConfig.INTS_CHANNEL_2D4G.length; i++) {
            if (channelSum[AppConfig.INTS_CHANNEL_2D4G[i]] != 0) {
                y.add(new PieEntry(channelSum[AppConfig.INTS_CHANNEL_2D4G[i]], "CH" + AppConfig.INTS_CHANNEL_2D4G[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(y, "2.4G");
        dataSet.setDrawValues(false);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setSliceSpace(2f);
        dataSet.setColor(ContextCompat.getColor(this.getActivity(), R.color.chartLineColor4));
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) value) + "";
            }
        });
//        dataSet.setValueTextSize(10);
//        dataSet.setValueTextColor(Color.WHITE);
//        dataSet.setValueLinePart1Length(0.23f);
//        dataSet.setValueLinePart2Length(0.15f);
//        dataSet.setValueLineColor(Color.WHITE);
        PieData pieData = new PieData(dataSet);
        wifiChannel2d4g.setData(pieData);
        wifiChannel2d4g.invalidate();
    }

    private void recoveryPie2ChartBaseData(int channelSum[]) {
        if (wifiChannel5g.getData() != null) {
            wifiChannel5g.setData(null);
        }
        List<PieEntry> y = new ArrayList<>();
        for (int i = 0; i < AppConfig.INTS_CHANNEL_5G.length; i++) {
            if (channelSum[AppConfig.INTS_CHANNEL_5G[i]] != 0) {
                y.add(new PieEntry(channelSum[AppConfig.INTS_CHANNEL_5G[i]], "CH" + AppConfig.INTS_CHANNEL_5G[i]));
            }
        }
        if (y.size() == 0) {
            for (int i = 0; i < AppConfig.INTS_CHANNEL_5G.length; i++) {
                y.add(new PieEntry(5, "CH" + AppConfig.INTS_CHANNEL_5G[i]));
            }
        }
        PieDataSet dataSet = new PieDataSet(y, "5G");
        dataSet.setDrawValues(false);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) value) + "";
            }
        });

        dataSet.setSliceSpace(2f);
        dataSet.setColor(ContextCompat.getColor(this.getActivity(), R.color.chartLineColor3));
//        dataSet.setValueTextSize(10);
//        dataSet.setValueTextColor(Color.WHITE);
//        dataSet.setValueLinePart1Length(0.23f);
//        dataSet.setValueLinePart2Length(0.15f);
//        dataSet.setValueLineColor(Color.WHITE);
        PieData pieData = new PieData(dataSet);
        wifiChannel5g.setData(pieData);
        wifiChannel5g.invalidate();
    }

    private void recoveryBarChartBaseData(String is2d4g, int channelSum[]) {
        final boolean b_2d4g = is2d4g.equals("true");
        if (wifiChannelCurrent.getData() != null) {
            wifiChannelCurrent.setData(null);
        }
        final BarDataSet barDataSet;
        ArrayList<BarEntry> y = new ArrayList<>();
        if (b_2d4g) {
            for (int i = 0; i < AppConfig.INTS_CHANNEL_2D4G.length; i++) {//添加数据源
                if (channelSum[i] == 0) {
                    y.add(new BarEntry(i, 0.5f));
                } else {
                    y.add(new BarEntry(i, channelSum[i]));
                }
            }
            barDataSet = new BarDataSet(y, "2.4G");
            barDataSet.setColor(ContextCompat.getColor(this.getActivity(), R.color.chartLineColor4));//设置第一组数据颜色
            wifiChannelCurrent.getXAxis().setAxisMinimum(-0.5f);
            wifiChannelCurrent.getXAxis().setAxisMaximum(12.5f);
        } else {
            for (int i = 0; i < AppConfig.INTS_CHANNEL_5G.length; i++) {//添加数据源
                if (channelSum[i] == 0) {
                    y.add(new BarEntry(1 + i * 2, 0.5f));
                } else {
                    y.add(new BarEntry(1 + i * 2, channelSum[i]));
                }
            }
            barDataSet = new BarDataSet(y, "5G");
            barDataSet.setColor(ContextCompat.getColor(this.getActivity(), R.color.chartLineColor3));//设置第一组数据颜色
            wifiChannelCurrent.getXAxis().setAxisMinimum(0.5f);
            wifiChannelCurrent.getXAxis().setAxisMaximum(9.5f);
        }
        barDataSet.setValueTextColor(0xFFFFFFFF);
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value >= 1) {
                    return ((int) value) + "";
                } else {
                    return "0";
                }
            }
        });
        ArrayList<IBarDataSet> iDataSets = new ArrayList<>();//IBarDataSet 接口很关键，是添加多组数据的关键结构，LineChart也是可以采用对应的接口类，也可以添加多组数据
        iDataSets.add(barDataSet);
        BarData bardata = new BarData(iDataSets);
        wifiChannelCurrent.setData(bardata);
        wifiChannelCurrent.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (!b_2d4g) {
                    if (((int) value) % 2 == 1) {
                        return "C" + AppConfig.INTS_CHANNEL_5G[((int) value) % 2];
                    } else {
                        return "";
                    }

                } else {
                    return "C" + AppConfig.INTS_CHANNEL_2D4G[(int) value];
                }
            }
        });
        wifiChannelCurrent.invalidate();
    }


    private void popOperatorMenu() {
//        bottomDialog.show();
    }
}
