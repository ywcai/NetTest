package ywcai.ls.mobileutil.tools.Wifi.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import mehdi.sakout.fancybuttons.FancyButton;
import rx.Observable;
import rx.functions.Action1;
import ywcai.ls.control.flex.FlexButtonLayout;
import ywcai.ls.control.flex.OnFlexButtonClickListener;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.tools.Wifi.model.one.RadarMarkerView;
import ywcai.ls.mobileutil.tools.Wifi.model.one.WifiDrawDetail;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;


public class WifiFragmentOne extends Fragment {
    private View view;
    private RadarChart mChart;
    private MainWifiActionInf mainWifiActionInf;
    private FlexButtonLayout chooseChannel2d4G, chooseChannel5G;

    public void setGlobalAction(MainWifiActionInf _mainWifiActionInf) {
        this.mainWifiActionInf = _mainWifiActionInf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_wifi_first, container, false);
        createRadarChart();
        createOperatorBtn();
        create2d4GTags();
        create5GTags();
        return view;
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
    public void updateDeviceList(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.wifi_refresh_first_info:
                refreshUi(event.obj);
                break;
            case GlobalEventT.wifi_set_lock_btn_status:
                lockChart((Boolean) event.obj);
                setLockBtnStatus((Boolean) event.obj);
                break;
            case GlobalEventT.wifi_recovery_channel_select_2d4g:

                recovery2d4GTags((int[])event.obj);
                break;
            case GlobalEventT.wifi_recovery_channel_select_5g:

                recovery5GTags((int[])event.obj);
                break;
            case GlobalEventT.wifi_set_channel_btn_status:
                refreshChannelTagVisible((Boolean) event.obj);
                break;
            case GlobalEventT.wifi_set_lock_save_btn_visible:
                updateLockAndSaveBtnVisible((Boolean) event.obj);
                break;
            case GlobalEventT.wifi_set_task_btn_status:
                setTaskBtnStatus((Boolean) event.obj);
                break;
            case GlobalEventT.wifi_set_select_entry_info:
                showSelectEntryInfo((WifiEntry) event.obj);
                break;
            case GlobalEventT.wifi_set_first_main_toast:
                showToast(event.tip);
                break;
        }
    }

    private void showToast(String tip) {
        Toast.makeText(this.getContext(), tip, Toast.LENGTH_SHORT).show();
    }


    private void createRadarChart() {
        mChart = (RadarChart) view.findViewById(R.id.chart1);
        mChart.setBackgroundColor(0x606582);//暗灰色，颜色不错
        mChart.setNoDataText("Loading...");
        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.LTGRAY);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.LTGRAY);
        mChart.setWebAlpha(100);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(8f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(20f);
        xAxis.setSpaceMax(400);
        xAxis.setSpaceMin(400);
        xAxis.setAvoidFirstLastClipping(true); //设置X轴第一个和最后一个标签不能超出屏幕
        YAxis yAxis = mChart.getYAxis();
        yAxis.setLabelCount(5, true);//true,精确绘制坐标轴数量
        yAxis.setTextSize(8f);
        yAxis.setAxisMinimum(-160);
        yAxis.setAxisMaximum(-40);
        yAxis.setSpaceMin(200);//设置坐标轴额外的最小的空间
        yAxis.setSpaceMax(200); //设置坐标轴额外的最大的空间
        yAxis.setDrawLabels(false);
        yAxis.setCenterAxisLabels(true);
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        Description desc = new Description();
        desc.setTextColor(Color.WHITE);
        desc.setTextSize(14);
        desc.setText("");
        mChart.setDescription(desc);
        MarkerView mv = new RadarMarkerView(this.getContext(), R.layout.markerview_radar);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
    }

    private void createOperatorBtn() {
        ImageButton btnLock = (ImageButton) view.findViewById(R.id.btn_lock);
        ImageButton btnSave = (ImageButton) view.findViewById(R.id.btn_save);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.just("").throttleFirst(2, TimeUnit.SECONDS)
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                mainWifiActionInf.lockWifi();
                            }
                        });

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.just("").throttleFirst(2, TimeUnit.SECONDS)
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                mainWifiActionInf.addOrRemoveTask();
                            }
                        });
            }
        });
        btnLock.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
    }

    private void create2d4GTags() {
        chooseChannel2d4G = (FlexButtonLayout) view.findViewById(R.id.channel_list_2d4g);
        List<String> tag2d4g = new ArrayList<>();
        for (int i = 0; i < AppConfig.INTS_CHANNEL_2D4G.length; i++) {
            tag2d4g.add("CH" + AppConfig.INTS_CHANNEL_2D4G[i]);
        }
        chooseChannel2d4G.setDataAdapter(tag2d4g);
        chooseChannel2d4G.setOnFlexButtonClickListener(new OnFlexButtonClickListener() {
            @Override
            public void clickItem(int i, boolean b) {
                mainWifiActionInf.setChannelFilter(i,b);
            }

            @Override
            public void clickAllBtn(int[] ints, boolean b) {
                mainWifiActionInf.setAllTagSelectOrCancal(ints);
            }
        });
    }

    private void create5GTags() {
        chooseChannel5G = (FlexButtonLayout) view.findViewById(R.id.channel_list_5g);
        List<String> tag5g = new ArrayList<>();
        for (int i = 0; i < AppConfig.INTS_CHANNEL_5G.length; i++) {
            tag5g.add("CH" + AppConfig.INTS_CHANNEL_5G[i]);
        }
        chooseChannel5G.setDataAdapter(tag5g);
        chooseChannel5G.setOnFlexButtonClickListener(new OnFlexButtonClickListener() {
            @Override
            public void clickItem(int i, boolean b) {
                mainWifiActionInf.setChannelFilter(i,b);
            }

            @Override
            public void clickAllBtn(int[] ints, boolean b) {
                mainWifiActionInf.setAllTagSelectOrCancal(ints);
            }
        });
    }

    private void updateChart(final WifiDrawDetail wifiDrawDetail) {
        if (mChart.getData() != null) {
            mChart.setData(null);
        }
        mChart.highlightValues(wifiDrawDetail.highlight);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                int selectPos = mChart.getData().getDataSetByIndex(0).getEntryIndex((RadarEntry) entry);
                mainWifiActionInf.saveHighLightHolderView(mChart.getHighlighted());
                mainWifiActionInf.setSelectEntry(wifiDrawDetail.wifiList.get(selectPos));
            }

            @Override
            public void onNothingSelected() {
                mainWifiActionInf.setSelectEntry(null);
            }
        });
        List<RadarEntry> radarEntryList = new ArrayList<>();
        for (int i = 0; i < wifiDrawDetail.wifiList.size(); i++) {
            RadarEntry radarEntry = new RadarEntry(wifiDrawDetail.wifiList.get(i).dbm);
            radarEntryList.add(radarEntry);
        }
        RadarDataSet set = new RadarDataSet(radarEntryList, "");
        set.setColor(Color.rgb(121, 162, 175));
        set.setDrawFilled(true);
        set.setFillAlpha(80);
        set.setLineWidth(2f);
        set.setDrawHighlightCircleEnabled(true);
        set.setDrawHighlightIndicators(false);
        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set);
        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(true);
        data.setValueTextColor(Color.WHITE);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= wifiDrawDetail.wifiList.size() || value < 0) {
                    return "";
                }
                return wifiDrawDetail.wifiList.get((int) value).ssid.equals("") ? wifiDrawDetail.wifiList.get((int) value).bssid
                        : wifiDrawDetail.wifiList.get((int) value).ssid;
            }
        });
        setChannelStatus(wifiDrawDetail.channelNum);
        mChart.setData(data);
        mChart.invalidate();
    }


    private void updateLockAndSaveBtnVisible(Boolean isSelect) {
        ImageButton btnLock = (ImageButton) view.findViewById(R.id.btn_lock);
        ImageButton btnSave = (ImageButton) view.findViewById(R.id.btn_save);
        if (isSelect) {
            btnLock.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
        } else {
            btnLock.setVisibility(View.INVISIBLE);
            btnSave.setVisibility(View.INVISIBLE);
        }
    }

    public void setLockBtnStatus(Boolean isLock) {
        ImageButton btnLock = (ImageButton) view.findViewById(R.id.btn_lock);
        if (isLock) {
            btnLock.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.unlock));
        } else {
            btnLock.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.lock));
        }
    }

    public void setTaskBtnStatus(Boolean isExistTask) {
        ImageButton btnSave = (ImageButton) view.findViewById(R.id.btn_save);
        if (!isExistTask) {
            btnSave.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.add));
        } else {
            btnSave.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.remove));
        }
    }

    //
    private void recovery2d4GTags(int[] obj) {
        chooseChannel2d4G.setSelectIndex(obj);
    }

    private void recovery5GTags(int[] obj) {
        chooseChannel5G.setSelectIndex(obj);
    }

    private void refreshChannelTagVisible(Boolean is2d4G) {
        chooseChannel2d4G = (FlexButtonLayout) view.findViewById(R.id.channel_list_2d4g);
        chooseChannel5G = (FlexButtonLayout) view.findViewById(R.id.channel_list_5g);
        if (is2d4G) {
            chooseChannel2d4G.setVisibility(View.VISIBLE);
            chooseChannel5G.setVisibility(View.INVISIBLE);
        } else {
            chooseChannel2d4G.setVisibility(View.INVISIBLE);
            chooseChannel5G.setVisibility(View.VISIBLE);
        }
    }


    private void lockChart(Boolean isLock) {
        mChart.setTouchEnabled(!isLock);
        mChart.invalidate();
    }

    private void refreshUi(Object obj) {
        WifiDrawDetail wifiDrawDetail = (WifiDrawDetail) obj;
        updateChart(wifiDrawDetail);
    }


    public void setChannelStatus(int[] sum) {
        for (int i = 0; i < chooseChannel2d4G.getChildCount() - 1; i++) {
            if (sum[AppConfig.INTS_CHANNEL_2D4G[i]] == 0) {
                chooseChannel2d4G.setBtnSecondaryBgColor(i, ContextCompat.getColor(this.getActivity(),
                        R.color.LDarkLight));
            } else {
                //恢复背景色
                chooseChannel2d4G.setBtnSecondaryBgColor(i, ContextCompat.getColor(this.getActivity()
                        , R.color.LOrange));
            }
        }
        for (int i = 0; i < chooseChannel5G.getChildCount() - 1; i++) {
            if (sum[AppConfig.INTS_CHANNEL_5G[i]] == 0) {
                chooseChannel5G.setBtnSecondaryBgColor(i, ContextCompat.getColor(this.getActivity(),
                        R.color.LDarkLight));
            } else {
                //恢复背景色
                chooseChannel5G.setBtnSecondaryBgColor(i, ContextCompat.getColor(this.getActivity()
                        , R.color.LOrange));
            }
        }
    }


    public void showSelectEntryInfo(WifiEntry wifiEntry) {
        TextView mainInfo = (TextView) view.findViewById(R.id.main_select_info);
        if (wifiEntry == null) {
            mainInfo.setText("");
        } else {
            if (wifiEntry.dbm <= -160) {
                String desc1 = "[" + wifiEntry.ssid
                        + "]   [" + wifiEntry.bssid
                        + "]   [CH" + wifiEntry.channel
                        + "]   [信号丢失]";
                mainInfo.setText(desc1);
            } else {
                String desc2 = "[" + wifiEntry.ssid
                        + "]   [" + wifiEntry.bssid
                        + "]   [CH" + wifiEntry.channel
                        + "]   [" + wifiEntry.dbm
                        + "db]";
                if (wifiEntry.isConnWifi) {
                    desc2 += "  [" + wifiEntry.device + "]" +
                            "   [速度" + wifiEntry.speed + "M]" +
                            "   [当前连接]";
                }
                mainInfo.setText(desc2);
            }
        }
    }

}
