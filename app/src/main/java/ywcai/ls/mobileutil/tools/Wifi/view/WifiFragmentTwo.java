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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
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

import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsNotification;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;
import ywcai.ls.mobileutil.service.LsPendingIntent;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;
import ywcai.ls.mobileutil.tools.Wifi.model.two.WifiTaskAdapter;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;

import static android.content.Context.NOTIFICATION_SERVICE;


public class WifiFragmentTwo extends Fragment {
    private View view;
    private LineChart wifiTaskChart;
    private RecyclerView wifiTaskRecyclerView;
    private List<WifiEntry> wifiTaskList;
    private WifiTaskAdapter wifiTaskAdapter;
    private MaterialDialog bottomDialog;
    private MainWifiActionInf mainWifiActionInf;
    private int popTaskPos = 0;
    private int baseMaxX = 100;
    private Button save_local, save_remote, clear, cancal;
    private boolean isReceiveFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_wifi_two, container, false);
        createChart();
        createRecycler();
        createPopDialog();
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void update(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.wifi_refresh_two_list:
                if (isReceiveFlag) {
                    updateRecycler((List<WifiEntry>) event.obj);
                }
                break;
            case GlobalEventT.global_pop_operator_dialog:
                popOperatorMenu();
                break;
            case GlobalEventT.wifi_refresh_two_chart_line:
                recoveryBaseData();
                break;
            case GlobalEventT.wifi_set_item_btn_hide_status:
                setItemBtnStatus(Integer.parseInt(event.tip), (Boolean) event.obj);
                break;
            case GlobalEventT.wifi_notify_top_notification:
                popNotification(event.tip);
                break;
            case GlobalEventT.wifi_set_receive_flag:
                //避免前一次打开的对象无法销毁，增加该标识
                isReceiveFlag = true;
                break;
        }
    }

    private void popNotification(String tip) {
        LsNotification.notification(this.getActivity(), tip, AppConfig.TITLE_WIFI, AppConfig.WIFI_ACTIVITY_PATH,
                R.drawable.homepage_menu_wifi
                , AppConfig.INT_NOTIFICATION_PID_WIFI);
    }

    private void createPopDialog() {
        bottomDialog = new MaterialDialog(this.getActivity());
        bottomDialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.pop_dialog_wifi, null);
        save_local = (Button) view.findViewById(R.id.wifi_save_local);
        save_remote = (Button) view.findViewById(R.id.wifi_save_remote);
        clear = (Button) view.findViewById(R.id.wifi_clear);
        cancal = (Button) view.findViewById(R.id.wifi_cancal);
        bottomDialog.setContentView(view);
        save_local.setClickable(true);
        save_remote.setClickable(true);
        clear.setClickable(true);
        cancal.setClickable(true);
        save_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                mainWifiActionInf.saveLogForLocal(popTaskPos);
            }
        });
        save_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                mainWifiActionInf.saveLogForRemote(popTaskPos);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                mainWifiActionInf.clearLog(popTaskPos);
            }
        });
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });

    }


    private void updateRecycler(List<WifiEntry> obj) {
        wifiTaskList.clear();
        wifiTaskList.addAll(obj);
        wifiTaskAdapter.notifyDataSetChanged();
    }


    private void setItemBtnStatus(int pos, Boolean isShowInChart) {
        wifiTaskList.get(pos).isShowInChart = isShowInChart;
        wifiTaskAdapter.notifyDataSetChanged();
    }


    private void createChart() {
        wifiTaskChart = (LineChart) view.findViewById(R.id.wifi_task_chart);
        wifiTaskChart.getAxisRight().setEnabled(false);
        wifiTaskChart.setDrawGridBackground(false);
        wifiTaskChart.setNoDataText("请在第一页雷达图中选择您要记录的信号");
        wifiTaskChart.setTouchEnabled(true);
        wifiTaskChart.setDragEnabled(true);
        wifiTaskChart.setScaleEnabled(true);
        wifiTaskChart.setDoubleTapToZoomEnabled(false);
        wifiTaskChart.setHighlightPerTapEnabled(false);
        wifiTaskChart.setHighlightPerDragEnabled(false);
        LimitLine ll2 = new LimitLine(-160, "LOSS");
        ll2.setTextColor(Color.WHITE);
        ll2.setLineWidth(2f);
        ll2.enableDashedLine(10, 20f, 0);
        ll2.setTextSize(8);
        ll2.setLineColor(Color.DKGRAY);
        YAxis leftAxis = wifiTaskChart.getAxisLeft();
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(-20);
        leftAxis.setAxisMinimum(-160);
        leftAxis.setLabelCount(5, false);
        leftAxis.setTextSize(10);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextColor(Color.WHITE);
        XAxis xAxis = wifiTaskChart.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.setAxisMaximum(baseMaxX);
        xAxis.setLabelCount(10, false);
        xAxis.setAxisMinimum(0);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.WHITE);
        Description des = new Description();
        des.setText("");
        wifiTaskChart.setDescription(des);
        wifiTaskChart.getLegend().setWordWrapEnabled(true);
        wifiTaskChart.getLegend().setTextSize(6);
        wifiTaskChart.getLegend().setTextColor(Color.WHITE);
        wifiTaskChart.setAutoScaleMinMaxEnabled(true);
        recoveryBaseData();
    }

    private void recoveryBaseData() {
        final WifiState wifiState = CacheProcess.getInstance().getWifiState();
        if (wifiState.saveWifiList.size() == 0) {
            wifiTaskChart.setData(null);
            wifiTaskChart.invalidate();
            return;
        }
        Observable.from(wifiState.saveWifiList)
                .filter(new Func1<WifiEntry, Boolean>() {
                    @Override
                    public Boolean call(WifiEntry wifiEntry) {
                        return wifiEntry.isShowInChart;
                    }
                })
                .map(new Func1<WifiEntry, ILineDataSet>() {
                    @Override
                    public ILineDataSet call(WifiEntry wifiEntry) {
                        int maxX = (int) wifiTaskChart.getXChartMax();
                        String legend = wifiEntry.ssid;
                        List<Entry> chartData = new ArrayList<>();
                        List data = CacheProcess.getInstance().getWifiTaskResult(wifiEntry.logFlag);
                        if (maxX < data.size()) {
                            wifiTaskChart.getXAxis().setAxisMaximum((maxX + baseMaxX));
                        }
                        if (data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                Entry entry = new Entry(i, Float.parseFloat(data.get(i) + ""));
                                chartData.add(entry);
                            }
                        } else {
                            chartData.add(new Entry(0, Float.parseFloat(wifiEntry.dbm + "")));
                        }
                        LineDataSet dataSet = new LineDataSet(chartData, legend);//图列名称
                        dataSet.setDrawCircles(false);
                        dataSet.setDrawValues(false);
                        dataSet.setHighlightEnabled(false);
                        dataSet.setColor(ContextCompat.getColor(getActivity(), AppConfig.colors[wifiState.saveWifiList.indexOf(wifiEntry)]));
                        dataSet.setLineWidth(1.5f);
                        return dataSet;
                    }
                }).toList()
                .subscribe(new Action1<List<ILineDataSet>>() {
                    @Override
                    public void call(List<ILineDataSet> iLineDataSets) {
                        if (wifiTaskChart.getData() != null) {
                            wifiTaskChart.setData(null);
                        }
                        LineData lineData = new LineData(iLineDataSets);
                        wifiTaskChart.setData(lineData);
                        wifiTaskChart.invalidate();
                    }
                });
    }

    private void createRecycler() {
        wifiTaskRecyclerView = (RecyclerView) view.findViewById(R.id.task_recycler_list);
        List list = CacheProcess.getInstance().getWifiState().saveWifiList;
        wifiTaskList = new ArrayList<>();
//        wifiTaskList.addAll(list);
        wifiTaskAdapter = new WifiTaskAdapter(this.getContext(), wifiTaskList);
        wifiTaskRecyclerView.setAdapter(wifiTaskAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(false);
        layoutManager.setReverseLayout(false);
        wifiTaskRecyclerView.setLayoutManager(layoutManager);
        wifiTaskAdapter.setOnclickListener(new OnItemClickListener() {
            @Override
            public void OnClickItem(View v, int pos) {
                popTaskPos = pos;
                mainWifiActionInf.clickTaskItem();
            }
        });
        wifiTaskAdapter.setBtnHideListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainWifiActionInf.showChartLine((int) v.getTag());
            }
        });
    }

    private void popOperatorMenu() {
        bottomDialog.show();
    }
}
