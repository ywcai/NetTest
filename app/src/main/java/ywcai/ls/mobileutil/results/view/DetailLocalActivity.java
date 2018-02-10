package ywcai.ls.mobileutil.results.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mobstat.StatService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rengwuxian.materialedittext.MaterialEditText;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


import me.drakeet.materialdialog.MaterialDialog;
import ywcai.ls.control.LoadingDialog;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsListTransfer;
import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.presenter.DetailLocalAction;
import ywcai.ls.mobileutil.results.presenter.inf.DetailActionInf;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;


@Route(path = "/results/view/DetailLocalActivity")
public class DetailLocalActivity extends AppCompatActivity {
    @Autowired()
    public int pos = 0;
    TextView record_remarks, record_detail, logTitle, logTime, logAddr, prev, next, pageNumber;
    LoadingDialog loadingDialog;
    DetailActionInf detailLocalAction;
    MaterialDialog uploadDialog, editDialog, deleteDialog;
    MaterialEditText inputText;
    RelativeLayout rl;
    LineChart chart;
    ResultState resultState = CacheProcess.getInstance().getResultState();
    List<LogIndex> temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_detail_local);
        initToolBar();
        initView();
        initDialog();
        loadingDialog.show();
        detailLocalAction.loadRecord(pos);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    private void initDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.pop_dialog_detail_local_input, null);
        inputText = (MaterialEditText) view.findViewById(R.id.detail_local_edit_input_text);
        loadingDialog = new LoadingDialog(this);
        deleteDialog = new MaterialDialog(this);
        editDialog = new MaterialDialog(this);
        uploadDialog = new MaterialDialog(this);
        loadingDialog.show();
        deleteDialog.setCanceledOnTouchOutside(true);
        deleteDialog.setTitle("删除");
        deleteDialog.setMessage("确定删除这条记录?");
        deleteDialog.setNegativeButton("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                loadingDialog.show();
                detailLocalAction.deleteRecord(pos);
            }
        });
        deleteDialog.setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        uploadDialog.setCanceledOnTouchOutside(true);
        uploadDialog.setTitle("上传");
        uploadDialog.setMessage("上传到云端后本地记录将被自动删除\n确定要上传到云端?");
        uploadDialog.setNegativeButton("上传", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDialog.dismiss();
                loadingDialog.show();
                detailLocalAction.uploadRecord(pos);
            }
        });
        uploadDialog.setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDialog.dismiss();
            }
        });

        editDialog.setCanceledOnTouchOutside(true);
        editDialog.setContentView(view);
        editDialog.setTitle("修改记录名称");
        editDialog.setNegativeButton("修改", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputText.length() >= inputText.getMinCharacters() && inputText.length() <= inputText.getMaxCharacters()) {
                    editDialog.dismiss();
                    loadingDialog.show();
                    detailLocalAction.editRecordTitle(pos, inputText.getText().toString());
                    inputText.setText("");
                } else {

                }
            }
        });
        editDialog.setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= inputText.getMinCharacters() && s.length() <= inputText.getMaxCharacters()) {
                    inputText.setError(null);
                } else {
                    inputText.setError("请按要求输入");
                }
            }
        });
    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_local_toolbar);
        toolbar.setTitleMarginStart(0);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pageNumber = (TextView) findViewById(R.id.detail_local_page_number);
        prev = (TextView) findViewById(R.id.detail_local_prev);
        next = (TextView) findViewById(R.id.detail_local_next);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                detailLocalAction.prev(pos);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                detailLocalAction.next(pos);
            }
        });
    }

    private void initView() {
        chart = (LineChart) findViewById(R.id.detail_local_chart);
        chart.setNoDataText("该条记录无详细数据!");

        chart.setDrawGridBackground(false);
        chart.getAxisRight().setEnabled(false);

        chart.setDescription(null);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setNoDataTextColor(ContextCompat.getColor(this, R.color.colorWifiBg));

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.colorWifiBg));
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setTextColor(ContextCompat.getColor(this, R.color.colorWifiBg));


        detailLocalAction = new DetailLocalAction(resultState, temp);
        record_remarks = (TextView) findViewById(R.id.detail_local_text_remark);
        record_detail = (TextView) findViewById(R.id.detail_local_text_detail);
        record_detail.setMovementMethod(new ScrollingMovementMethod());
        logTitle = (TextView) findViewById(R.id.detail_local_text_title);
        logTime = (TextView) findViewById(R.id.detail_local_text_time);
        logAddr = (TextView) findViewById(R.id.detail_local_text_addr);
        ImageView btn_upload = (ImageView) findViewById(R.id.detail_local_upload);
        ImageView btn_edit = (ImageView) findViewById(R.id.detail_local_edit_title);
        ImageView btn_delete = (ImageView) findViewById(R.id.detail_local_delete);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDialog.show();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.show();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });
        rl = (RelativeLayout) findViewById(R.id.detail_local_snack_container);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void update(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.detail_local_refresh_record:
                loadingDialog.dismiss();
                setPos(event.tip);
                loadRecord(event.obj);
                break;
            case GlobalEventT.global_pop_snack_tip:
                loadingDialog.dismiss();
                showSnackBar(event.tip, (boolean) event.obj);
                break;
        }
    }

    private void setPos(String tip) {
        pos = Integer.parseInt(tip);
        pageNumber.setText((pos + 1) + "/" + temp.size());
        if (pos >= temp.size() - 1) {
            next.setClickable(false);
            next.setTextColor(ContextCompat.getColor(this, R.color.cardview_shadow_start_color));
        } else {
            next.setClickable(true);
            next.setTextColor(ContextCompat.getColor(this, R.color.cardview_light_background));
        }
        if (pos < 1) {
            prev.setClickable(false);
            prev.setTextColor(ContextCompat.getColor(this, R.color.cardview_shadow_start_color));
        } else {
            prev.setClickable(true);
            prev.setTextColor(ContextCompat.getColor(this, R.color.cardview_light_background));
        }
    }

    private void loadRecord(Object obj) {
        if (obj == null) {
            updateNormalInfo(null);
            chart.setData(null);
            chart.invalidate();
            setRemarks("当前无数据");
            setDetail("当前无数据");
            return;
        }
        LogIndex logIndex = (LogIndex) obj;
        updateNormalInfo(logIndex);
        switch (logIndex.cacheTypeIndex) {
            case AppConfig.INDEX_PING:
                updateForPing(logIndex);
                break;
            case AppConfig.INDEX_WIFI:
                updateForWifi(logIndex);
                break;
            case AppConfig.INDEX_STATION:
                updateForStation(logIndex);
                break;
            default:
                updateForOther(logIndex);
                break;
        }

    }

    private void updateNormalInfo(LogIndex logIndex) {
        if (logIndex != null) {
            logTitle.setText(logIndex.aliasFileName);
            logTime.setText(logIndex.logTime);
            logAddr.setText(logIndex.addr);
            inputText.setText(logIndex.aliasFileName);
        } else {
            logTitle.setText("");
            logTime.setText("");
            logAddr.setText("");
            inputText.setText("");
        }
    }

    private void updateForPing(LogIndex logIndex) {
        String remarks = logIndex.remarks;
        List<Float> list = CacheProcess.getInstance().getPingResult(logIndex.cacheFileName);
        String detail = LsListTransfer.floatToString(list, "ms");
        detail = detail.replace("-40.0ms", "丢包");
        detail = detail.replace("[", "");
        detail = detail.replace("]", "");
        setRemarks(remarks);
        setDetail(detail);
        updateChartForPing(list);
    }

    private void updateForWifi(LogIndex logIndex) {
        String remarks = logIndex.remarks;
        List<Integer> list = CacheProcess.getInstance().getWifiTaskResult(logIndex.cacheFileName);
        String detail = LsListTransfer.intToString(list, "db");
        detail = detail.replace("-160db", "无信号");
        detail = detail.replace("[", "");
        detail = detail.replace("]", "");
        setRemarks(remarks);
        setDetail(detail);
        updateChartForWifi(list);
    }

    private void updateForStation(LogIndex logIndex) {
        String remarks = logIndex.remarks;
        List<StationEntry> list = CacheProcess.getInstance().getStationRecord(logIndex.cacheFileName);
        String detail = "";
        for (int i = 0; i < list.size(); i++) {
            detail += list.get(i).toString();
            if (i < list.size() - 1) {
                detail += "\n";
            }
        }
        setRemarks(remarks);
        setDetail(detail);
        updateChartForStation(list);
    }

    private void updateForOther(LogIndex logIndex) {
        String remarks = logIndex.remarks;
        setRemarks(remarks);
        setDetail("该条记录无详细数据!");
        updateChartForOther();
    }


    private void showSnackBar(String tip, boolean success) {
        if (success) {
            LsSnack.show(this, rl, tip);
        } else {
            LsSnack.show(this, rl, tip, R.color.LRed);
        }
    }

    private void setRemarks(String tip) {
        record_remarks.setText(tip);
    }

    private void setDetail(String tip) {
        record_detail.setText(tip);
    }

    private void updateChartForPing(List<Float> list) {
        int xMax = list.size();
        float yMax = LsListTransfer.getFloatMax(list) + 10;
        LimitLine ll2 = new LimitLine(-40, "丢包");
        ll2.setLineWidth(0.5f);
        ll2.setTextSize(8);
        ll2.setLineColor(Color.RED);
        ll2.setTextColor(Color.DKGRAY);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(4);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(yMax);
        leftAxis.setAxisMinimum(-40);
        leftAxis.setTextSize(6);
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(4);
        xAxis.setTextSize(6);
        xAxis.removeAllLimitLines();
        xAxis.enableGridDashedLine(10, 20f, 0);
        xAxis.setAxisMaximum(xMax);
        List<Entry> chartData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            chartData.add(new Entry(i, list.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(chartData, "PING");//图列名称
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.GREEN);
        dataSet.setLineWidth(1.2f);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        lineData.setDrawValues(false);
        chart.setData(lineData);
        chart.getLegend().setTextColor(Color.DKGRAY);
        chart.getLegend().setTextSize(6);
        chart.invalidate();

    }

    private void updateChartForWifi(List<Integer> list) {
        int xMax = list.size() - 1;
        LimitLine ll2 = new LimitLine(-160, "信号丢失");
        ll2.setLineWidth(0.5f);
        ll2.setTextSize(8);
        ll2.setLineColor(Color.RED);
        ll2.setTextColor(Color.DKGRAY);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(4);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(-20);
        leftAxis.setAxisMinimum(-160);
        leftAxis.setTextSize(6);
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(4);
        xAxis.removeAllLimitLines();
        xAxis.enableGridDashedLine(10, 20f, 0);
        xAxis.setAxisMaximum(xMax);
        xAxis.setAxisMinimum(0);
        xAxis.setTextSize(6);
        List<Entry> chartData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            chartData.add(new Entry(i, list.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(chartData, "WIFI");//图列名称
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.GREEN);
        dataSet.setLineWidth(1.2f);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        lineData.setDrawValues(false);
        chart.setData(lineData);
        chart.getLegend().setTextColor(Color.DKGRAY);
        chart.getLegend().setTextSize(6);
        chart.invalidate();
    }

    private void updateChartForStation(List<StationEntry> list) {
        int xMax = list.size() - 1;
        LimitLine ll2 = new LimitLine(-160, "信号丢失");
        ll2.setLineWidth(0.5f);
        ll2.setTextSize(8);
        ll2.setLineColor(Color.RED);
        ll2.setTextColor(Color.DKGRAY);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll2);
        leftAxis.setLabelCount(4);
        leftAxis.setAxisMaximum(-20);
        leftAxis.setAxisMinimum(-160);
        leftAxis.setTextSize(6);
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(4);
        xAxis.removeAllLimitLines();
        xAxis.setAxisMaximum(xMax);
        xAxis.setAxisMinimum(0);
        xAxis.setTextSize(6);
        List<Entry> chartData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int y = list.get(i).rsp == 0 ? -160 : list.get(i).rsp;
            chartData.add(new Entry(i, y));
        }
        LineDataSet dataSet = new LineDataSet(chartData, "基站信号");//图列名称
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.GREEN);
        dataSet.setLineWidth(1.2f);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        lineData.setDrawValues(false);
        chart.setData(lineData);
        chart.getLegend().setTextColor(Color.DKGRAY);
        chart.getLegend().setTextSize(6);
        chart.invalidate();
    }

    private void updateChartForOther() {
        chart.setData(null);
        chart.invalidate();
    }

}
