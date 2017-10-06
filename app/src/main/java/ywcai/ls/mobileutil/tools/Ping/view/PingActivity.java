package ywcai.ls.mobileutil.tools.Ping.view;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
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
import mehdi.sakout.fancybuttons.FancyButton;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.model.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.InputValidate;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.Ping.model.PingState;
import ywcai.ls.mobileutil.tools.Ping.presenter.PingAction;
import ywcai.ls.mobileutil.tools.Ping.presenter.inf.PingActionInf;

public class PingActivity extends AppCompatActivity {
    private final float yMax = 230;
    private LineChart pingResultChart;
    private PingActionInf action=new PingAction();
    private MaterialDialog bottomDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_ping);
        InitToolBar();
        InitChart();
        InitView();
        InitPopView();

    }

    private void InitPopView() {
        InitBottomDialog();
        InitFloatingEvent();
    }

    private void setFloatingBtnVisible(int visible)
    {
        FloatingActionButton showResultBtn=(FloatingActionButton)findViewById(R.id.f_btn_show_task);
        showResultBtn.setVisibility(visible);
    }
    private void InitFloatingEvent()
    {
        FloatingActionButton showResultBtn=(FloatingActionButton)findViewById(R.id.f_btn_show_task);
        showResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.clickBtnFloating();
            }
        });
    }

    private void InitBottomDialog() {
        bottomDialog = new MaterialDialog(this);
        bottomDialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(this).inflate(R.layout.ping_dialog_pop, null);
        bottomDialog.setContentView(view);
        Button save_local = (Button) view.findViewById(R.id.ping_save_local);
        Button save_remote = (Button) view.findViewById(R.id.ping_save_remote);
        Button clear = (Button) view.findViewById(R.id.ping_clear);
        Button cancal = (Button) view.findViewById(R.id.ping_cancal);
        save_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.clickBtnSaveLocal();
                bottomDialog.dismiss();
            }
        });
        save_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.clickBtnSaveRemote();
                bottomDialog.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                action.clickBtnSaveClear();
                bottomDialog.dismiss();
            }
        });
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.clickBtnSaveCancal();
                bottomDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        action.activityResume();
        super.onResume();

    }
    private void popBottomDialog() {
        bottomDialog.show();
    }
    private void updateCacheChartData(List<Float> list) {
        LineData lineData = pingResultChart.getLineData();
        ILineDataSet iLineDataSet = lineData.getDataSetByIndex(0);
        if (list != null) {
            int min=iLineDataSet.getEntryCount()<list.size()?iLineDataSet.getEntryCount():list.size();
            for (int i = 0; i < min; i++) {
                Entry entry = iLineDataSet.getEntryForIndex(i);
                entry.setY(list.get(i));
            }
        }
        pingResultChart.invalidate();
    }

    private void InitView() {
        final FancyButton pingTest = (FancyButton) findViewById(R.id.test_start);
        RangeBar rangeBar = (RangeBar) findViewById(R.id.packageCounts);
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                action.changeBarPackageNumber(Integer.parseInt(rightPinValue));
            }
        });
        RangeBar rangeBar2 = (RangeBar) findViewById(R.id.threadCounts);
        rangeBar2.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar2, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                action.changeBarThreadNumber(Integer.parseInt(rightPinValue));
            }
        });
        final MaterialEditText ipAddText = (MaterialEditText) findViewById(R.id.ping_ipaddr);
        ipAddText.setShowClearButton(true);
        ipAddText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ipAddress=s.toString();
                if (InputValidate.isIpAddr(ipAddress)) {
                    pingTest.setEnabled(true);
                } else {
                    ipAddress = "";
                    ipAddText.setError("请按IP地址格式输入");
                    pingTest.setEnabled(false);
                }
//                action.changeEditText(ipAddress);
            }
        });
        pingTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.clickBtnTest(ipAddText.getText().toString());
            }
        });
        FancyButton pauseAndResume = (FancyButton) findViewById(R.id.pause_resume);
        pauseAndResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.clickBtnPauseAndResume();
            }
        });
    }

    private void updateFormUIRun() {
        FancyButton pingTest = (FancyButton) findViewById(R.id.test_start);
        FancyButton pauseAndResume = (FancyButton) findViewById(R.id.pause_resume);
        RangeBar rangeBar = (RangeBar) findViewById(R.id.packageCounts);
        RangeBar rangeBar2 = (RangeBar) findViewById(R.id.threadCounts);
        MaterialEditText ipAddText = (MaterialEditText) findViewById(R.id.ping_ipaddr);
        pingTest.setIconResource(R.drawable.stop);
        pauseAndResume.setIconResource(R.drawable.pause);
        pauseAndResume.setEnabled(true);
        rangeBar.setEnabled(false);
        rangeBar2.setEnabled(false);
        ipAddText.setEnabled(false);
        ipAddText.setShowClearButton(false);
    }

    private void updateFormUIFree() {
        FancyButton pingTest = (FancyButton) findViewById(R.id.test_start);
        FancyButton pauseAndResume = (FancyButton) findViewById(R.id.pause_resume);
        RangeBar rangeBar = (RangeBar) findViewById(R.id.packageCounts);
        RangeBar rangeBar2 = (RangeBar) findViewById(R.id.threadCounts);
        MaterialEditText ipAddText = (MaterialEditText) findViewById(R.id.ping_ipaddr);
        pingTest.setIconResource(R.drawable.start);
        pauseAndResume.setIconResource(R.drawable.resume);
        pauseAndResume.setEnabled(false);
        rangeBar.setEnabled(true);
        rangeBar2.setEnabled(true);
        ipAddText.setEnabled(true);
        ipAddText.setShowClearButton(true);
    }
    private void updateFormUIPause() {
        FancyButton pingTest = (FancyButton) findViewById(R.id.test_start);
        FancyButton pauseAndResume = (FancyButton) findViewById(R.id.pause_resume);
        RangeBar rangeBar = (RangeBar) findViewById(R.id.packageCounts);
        RangeBar rangeBar2 = (RangeBar) findViewById(R.id.threadCounts);
        MaterialEditText ipAddText = (MaterialEditText) findViewById(R.id.ping_ipaddr);
        pingTest.setIconResource(R.drawable.stop);
        pauseAndResume.setIconResource(R.drawable.resume);
        pauseAndResume.setEnabled(true);
        rangeBar.setEnabled(false);
        rangeBar2.setEnabled(false);
        ipAddText.setEnabled(false);
        ipAddText.setShowClearButton(false);
    }


    private void setSeekBar1Pos(int value)
    {
        RangeBar rangeBar = (RangeBar) findViewById(R.id.packageCounts);
        rangeBar.setSeekPinByValue(value);
    }
    private void setSeekBar2Pos(int value)
    {
        RangeBar rangeBar2 = (RangeBar) findViewById(R.id.threadCounts);
        rangeBar2.setSeekPinByValue(value);
    }
    private void setInputValue(String input)
    {
        MaterialEditText ipAddText = (MaterialEditText) findViewById(R.id.ping_ipaddr);
        ipAddText.setText(input);
    }
    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.ping_toolbar);
        mToolbar.setTitleMarginStart(0);
        mToolbar.setTitle("Ping");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitChart() {
        pingResultChart = (LineChart) findViewById(R.id.pingResult);
        pingResultChart.setDrawGridBackground(false);
        pingResultChart.setTouchEnabled(true);
        pingResultChart.setDoubleTapToZoomEnabled(false);
        pingResultChart.setHighlightPerTapEnabled(false);
        pingResultChart.setHighlightPerDragEnabled(false);
        LimitLine ll2 = new LimitLine(-50, "LOSS");
        ll2.setLineWidth(0.2f);
        ll2.setTextSize(8);
        ll2.setLineColor(Color.RED);
        YAxis leftAxis = pingResultChart.getAxisLeft();
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(yMax);
        leftAxis.setAxisMinimum(-100);
        leftAxis.enableGridDashedLine(10, 20f, 0);
        XAxis xAxis = pingResultChart.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.enableGridDashedLine(10, 20f, 0);
        pingResultChart.getAxisRight().setEnabled(false);
    }

    private void updateChartData(int pos,float y) {
        LineData lineData = pingResultChart.getLineData();
        ILineDataSet iLineDataSet = lineData.getDataSetByIndex(0);
        Entry entry = iLineDataSet.getEntryForIndex(pos - 1);
        entry.setY(y);
        pingResultChart.invalidate();
    }

    private void drawDes(PingState realTimeState) {
        Description des = new Description();
        String des_ipAdd = "IP:" + realTimeState.ipAddress;
        String des_maxDelay = " MAX:" + realTimeState.max;
        String des_minDelay = " MIN:" + realTimeState.min;
        String des_avgDelay = " AVG:" + realTimeState.average;
        String des_loss = " LOSS:" + realTimeState.loss;
        String des_per = " " + realTimeState.per+"%";
        String des_send = " TOTAL:" + realTimeState.send;
        String des_all = des_ipAdd
                + des_maxDelay
                + des_minDelay
                + des_avgDelay
                + des_loss
                + des_per
                + des_send;
        des.setText(des_all);
        des.setTextSize(9);
        pingResultChart.setDescription(des);
        pingResultChart.invalidate();
    }

    //初始化X轴宽度及曲线基础数据
    private void setLineBase(int size) {
        String legend = "时延";
        int color = Color.GREEN;
        pingResultChart.clear();
        List<Entry> chartData = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            chartData.add(new Entry(i, 0));
        }
        LineDataSet dataSet = new LineDataSet(chartData, legend);//图列名称
        dataSet.setDrawCircles(false);
        dataSet.setColor(color);
        dataSet.setLineWidth(0.5f);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        pingResultChart.setData(lineData);
        pingResultChart.invalidate();
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
            case GlobalEventT.ping_update_chart_point:
                int pos=Integer.parseInt(event.tip);
                Float y = (Float) event.obj;
                updateChartData(pos,y);//更新曲线图
                break;
            case GlobalEventT.ping_update_chart_desc:
                PingState temp2 = (PingState) event.obj;
                drawDes(temp2);//更新描述
                break;
            case GlobalEventT.ping_set_bar_size_package:
                setSeekBar1Pos((int)event.obj);
                break;
            case GlobalEventT.ping_set_bar_size_thread:
                setSeekBar2Pos((int)event.obj);
                break;
            case GlobalEventT.ping_set_input_text_ip:
                setInputValue(event.obj.toString());
                break;
            case GlobalEventT.ping_set_chart_data_size:
                setLineBase((int)event.obj);
                break;
            case GlobalEventT.ping_repair_chart_line:
                List<Float> list=(List<Float>)event.obj;
                updateCacheChartData(list);
                break;
            case GlobalEventT.ping_set_form_free:
                updateFormUIFree();
                break;
            case GlobalEventT.ping_set_form_busy:
                updateFormUIRun();
                break;
            case GlobalEventT.ping_set_form_pause:
                updateFormUIPause();
                break;
            case GlobalEventT.ping_pop_operator_dialog:
                popBottomDialog();
                break;
            case GlobalEventT.ping_pop_loading_dialog:
                popLoadingDialog(event.tip);
                break;
            case GlobalEventT.ping_close_loading_dialog:
                closeLoadingDialog(event.tip);
                break;
            case GlobalEventT.ping_set_float_btn_visible:
                setFloatingBtnVisible((int)event.obj);
                break;
            default:
                break;
        }
    }



    private void closeLoadingDialog(String tip) {
        //// TODO: 2017/10/3 加载窗口
        Toast.makeText(this,tip,Toast.LENGTH_SHORT).show();
    }

    private void popLoadingDialog(String tip) {
        //// TODO: 2017/10/3 加载窗口
    }


}
