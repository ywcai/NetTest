package ywcai.ls.mobileutil.tools.Wifi.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;


public class WifiFragmentThree extends Fragment {
    private View view;
    private BarChart wifiChannelCurrent;
    private LineChart wifiFrequencyLevel;
    private PieChart wifiChannel2d4g, wifiChannel5g;
    private MainWifiActionInf mainWifiActionInf;


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
//        createPieChart1();
//        createPieChart2();
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

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void update(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.wifi_refresh_three_bar:
                recoveryBarChartBaseData(event.tip, (int[]) event.obj);
                break;
//            case GlobalEventT.wifi_refresh_three_pie:
//                recoveryPie1ChartBaseData((int[]) event.obj);
//                recoveryPie2ChartBaseData((int[]) event.obj);
//                break;
            case GlobalEventT.wifi_refresh_frequency_level:
                refreshLineData((int[]) event.obj);
                break;
        }
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
        wifiChannelCurrent.setNoDataText("");
        wifiChannelCurrent.setAutoScaleMinMaxEnabled(true);
    }

//    private void createPieChart1() {
//        wifiChannel2d4g = (PieChart) view.findViewById(R.id.wifi_three_pie_left);
//        wifiChannel2d4g.setTouchEnabled(false);
//        wifiChannel2d4g.getLegend().setEnabled(false);
//        wifiChannel2d4g.setDescription(null);
//        wifiChannel2d4g.setUsePercentValues(false);
//        wifiChannel2d4g.setHoleRadius(60);
//        wifiChannel2d4g.setTransparentCircleRadius(63);
//        wifiChannel2d4g.setDrawHoleEnabled(true);
//        wifiChannel2d4g.setHoleColor(Color.TRANSPARENT);
//        wifiChannel2d4g.setEntryLabelTextSize(10);
//        wifiChannel2d4g.setTransparentCircleColor(ContextCompat.getColor(this.getActivity(), R.color.colorWifiBg));
//        wifiChannel2d4g.setTransparentCircleAlpha(0);
//        wifiChannel2d4g.setNoDataText("");
//    }
//
//    private void createPieChart2() {
//        wifiChannel5g = (PieChart) view.findViewById(R.id.wifi_three_pie_right);
//        wifiChannel5g.setTouchEnabled(false);
//        wifiChannel5g.getLegend().setEnabled(false);
//        wifiChannel5g.setDescription(null);
//        wifiChannel5g.setUsePercentValues(false);
//        wifiChannel5g.setHoleRadius(60);
//        wifiChannel5g.setTransparentCircleRadius(63);
//        wifiChannel5g.setDrawHoleEnabled(true);
//        wifiChannel5g.setHoleColor(Color.TRANSPARENT);
//        wifiChannel5g.setEntryLabelTextSize(10);
//        wifiChannel5g.setTransparentCircleColor(ContextCompat.getColor(this.getActivity(), R.color.colorWifiBg));
//        wifiChannel5g.setTransparentCircleAlpha(0);
//        wifiChannel5g.setNoDataText("");
//    }
//
//    private void recoveryPie1ChartBaseData(int channelSum[]) {
//        if (wifiChannel2d4g.getData() != null) {
//            wifiChannel2d4g.setData(null);
//        }
////        List<String> x = new ArrayList<String>();
//        List<PieEntry> y = new ArrayList<>();
//        for (int i = 0; i < AppConfig.INTS_CHANNEL_2D4G.length; i++) {
//            if (channelSum[AppConfig.INTS_CHANNEL_2D4G[i]] != 0) {
//                y.add(new PieEntry(channelSum[AppConfig.INTS_CHANNEL_2D4G[i]], "CH" + AppConfig.INTS_CHANNEL_2D4G[i]));
//            }
//        }
//
//        PieDataSet dataSet = new PieDataSet(y, "2.4G");
//        dataSet.setDrawValues(false);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
////        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setSliceSpace(2f);
//        dataSet.setColor(ContextCompat.getColor(this.getActivity(), R.color.chartLineColor4));
//        dataSet.setValueFormatter(new IValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return ((int) value) + "";
//            }
//        });
////        dataSet.setValueTextSize(10);
////        dataSet.setValueTextColor(Color.WHITE);
////        dataSet.setValueLinePart1Length(0.23f);
////        dataSet.setValueLinePart2Length(0.15f);
////        dataSet.setValueLineColor(Color.WHITE);
//        PieData pieData = new PieData(dataSet);
//        wifiChannel2d4g.setData(pieData);
//        wifiChannel2d4g.invalidate();
//    }

//    private void recoveryPie2ChartBaseData(int channelSum[]) {
//        if (wifiChannel5g.getData() != null) {
//            wifiChannel5g.setData(null);
//        }
//        List<PieEntry> y = new ArrayList<>();
//        for (int i = 0; i < AppConfig.INTS_CHANNEL_5G.length; i++) {
//            if (channelSum[AppConfig.INTS_CHANNEL_5G[i]] != 0) {
//                y.add(new PieEntry(channelSum[AppConfig.INTS_CHANNEL_5G[i]], "CH" + AppConfig.INTS_CHANNEL_5G[i]));
//            }
//        }
//        if (y.size() == 0) {
//            for (int i = 0; i < AppConfig.INTS_CHANNEL_5G.length; i++) {
//                y.add(new PieEntry(5, "CH" + AppConfig.INTS_CHANNEL_5G[i]));
//            }
//        }
//        PieDataSet dataSet = new PieDataSet(y, "5G");
//        dataSet.setDrawValues(false);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
////        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setValueFormatter(new IValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return ((int) value) + "";
//            }
//        });
//
//        dataSet.setSliceSpace(2f);
//        dataSet.setColor(ContextCompat.getColor(this.getActivity(), R.color.chartLineColor5));
////        dataSet.setValueTextSize(10);
////        dataSet.setValueTextColor(Color.WHITE);
////        dataSet.setValueLinePart1Length(0.23f);
////        dataSet.setValueLinePart2Length(0.15f);
////        dataSet.setValueLineColor(Color.WHITE);
//        PieData pieData = new PieData(dataSet);
//        wifiChannel5g.setData(pieData);
//        wifiChannel5g.invalidate();
//    }

    private void recoveryBarChartBaseData(String is2d4g, int channelSum[]) {
        final boolean b_2d4g = is2d4g.equals("true");
        if (wifiChannelCurrent.getData() != null) {
            wifiChannelCurrent.setData(null);
        }
        BarDataSet barDataSet;
        ArrayList<BarEntry> y = new ArrayList<>();
        if (b_2d4g) {
            for (int i = 0; i < AppConfig.INTS_CHANNEL_2D4G.length; i++) {//添加数据源
                if (channelSum[i] == 0) {
                    y.add(new BarEntry(i, 0.5f));
                }
//                else if (channelSum[i] > 20) {
//                    y.add(new BarEntry(i, 20f));
//                }
                else {
                    y.add(new BarEntry(i, channelSum[i]));
                }
                if (channelSum[i] >= wifiChannelCurrent.getYChartMax()) {
                    wifiChannelCurrent.getAxisLeft().setAxisMaximum(channelSum[i] + 1);
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
                }
//                else if (channelSum[i] > 20) {
//                    y.add(new BarEntry(1 + i * 2, 20f));
//                }
                else {
                    y.add(new BarEntry(1 + i * 2, channelSum[i]));
                }
                if (channelSum[i] >= wifiChannelCurrent.getYChartMax()) {
                    wifiChannelCurrent.getAxisLeft().setAxisMaximum(channelSum[i] + 5);
                }
            }
            barDataSet = new BarDataSet(y, "5G");
            barDataSet.setColor(ContextCompat.getColor(this.getActivity(), R.color.chartLineColor5));//设置第一组数据颜色
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
                    if (((int) value) == 1) {
                        return "CH" + AppConfig.INTS_CHANNEL_5G[0];
                    }
                    if (((int) value) == 3) {
                        return "CH" + AppConfig.INTS_CHANNEL_5G[1];
                    }
                    if (((int) value) == 5) {
                        return "CH" + AppConfig.INTS_CHANNEL_5G[2];
                    }
                    if (((int) value) == 7) {
                        return "CH" + AppConfig.INTS_CHANNEL_5G[3];
                    }
                    if (((int) value) == 9) {
                        return "CH" + AppConfig.INTS_CHANNEL_5G[4];
                    }
                    return "";
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

    private void createLineChart() {
        wifiFrequencyLevel = (LineChart) view.findViewById(R.id.wifi_frequency_level);
        wifiFrequencyLevel.getAxisRight().setEnabled(false);
        wifiFrequencyLevel.setDrawGridBackground(false);
        wifiFrequencyLevel.setNoDataText("");
        wifiFrequencyLevel.setTouchEnabled(true);
        wifiFrequencyLevel.setDragEnabled(true);
        wifiFrequencyLevel.setDoubleTapToZoomEnabled(false);
        wifiFrequencyLevel.setHighlightPerTapEnabled(false);
        wifiFrequencyLevel.setHighlightPerDragEnabled(false);
        YAxis leftAxis = wifiFrequencyLevel.getAxisLeft();
        leftAxis.setAxisMaximum(10);
        leftAxis.setAxisMinimum(0);
        leftAxis.setTextSize(10);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        leftAxis.enableGridDashedLine(10, 20f, 0);
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);
        XAxis xAxis = wifiFrequencyLevel.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.removeAllLimitLines();
        xAxis.setAxisMaximum(12);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisMinimum(0);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawAxisLine(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return "" + ((int) value + 1);
            }
        });
        Description des = new Description();
        des.setText("");
        wifiFrequencyLevel.setTouchEnabled(false);
        wifiFrequencyLevel.setDescription(null);
        wifiFrequencyLevel.getLegend().setEnabled(false);
        wifiFrequencyLevel.setAutoScaleMinMaxEnabled(true);
        createNewLineData();
    }

    private void createNewLineData() {
        int[] frequency = new int[13];
        List<ILineDataSet> listDataSets = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < frequency.length; i++) {
            entries.add(new Entry(i, 1));
        }
        LineDataSet dataSet = new LineDataSet(entries, "频谱分布");
        dataSet.setDrawCircles(true);
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setHighlightEnabled(false);
        dataSet.setFillAlpha(50);
        dataSet.setFillColor(ContextCompat.getColor(getActivity(), AppConfig.colors[9]));
        dataSet.setDrawFilled(true);
        dataSet.setColor(ContextCompat.getColor(getActivity(), AppConfig.colors[4]));
        dataSet.setLineWidth(1.5f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        listDataSets.add(dataSet);
        LineData chartData = new LineData(listDataSets);//图列名称
        wifiFrequencyLevel.setData(chartData);
        wifiFrequencyLevel.invalidate();
    }

    private void refreshLineData(final int[] frequency) {
        List<ILineDataSet> listDataSets = wifiFrequencyLevel.getLineData().getDataSets();
        LineDataSet dataSet = (LineDataSet) listDataSets.get(0);
        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            if (frequency[i + 2] >= wifiFrequencyLevel.getYChartMax()) {
                wifiFrequencyLevel.getAxisLeft().setAxisMaximum(frequency[i + 2] + 2);
            }
            dataSet.getEntryForIndex(i).setY(frequency[i + 2]);
        }
        wifiFrequencyLevel.invalidate();
    }
}
