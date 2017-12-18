package ywcai.ls.mobileutil.tools.ScanPort;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mobstat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.drakeet.materialdialog.MaterialDialog;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import ywcai.ls.control.scan.LsScan;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.InputValidate;

import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanPortResult;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanPortState;
import ywcai.ls.mobileutil.tools.ScanPort.presenter.ScanPortAction;
import ywcai.ls.mobileutil.tools.ScanPort.presenter.inf.ScanPortActionInf;

@Route(path = "/tools/ScanPort/view/ScanPortActivity")
public class ScanPortActivity extends AppCompatActivity {
    ScanPortActionInf actionInf;
    MaterialDialog materialDialog;
    FloatingActionButton popTaskMenu;
    View view;
    LsScan lsScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_scan_port);
        InitAction();
        InitToolBar();
        InitView();
    }

    private void InitAction() {
        actionInf = new ScanPortAction(this);
    }

    private void InitView() {
        lsScan = (LsScan) findViewById(R.id.radar_scan_port);
        ImageView btnOperator = (ImageView) findViewById(R.id.btn_scan_port_operator);
        btnOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInf.clickOperatorBtn();
            }
        });
        popTaskMenu = (FloatingActionButton) findViewById(R.id.btn_scan_port_cfg);
        popTaskMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu();
                popTaskMenu.hide();
            }
        });
        materialDialog = new MaterialDialog(this);
        view = LayoutInflater.from(this).inflate(R.layout.pop_dialog_scan_port_add_task, null);
        materialDialog.setContentView(view);
        materialDialog.setCanceledOnTouchOutside(true);
        materialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                popTaskMenu.show();
            }
        });
        Button subTask = (Button) view.findViewById(R.id.scan_port_sub_task);
        subTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    private void checkInput() {
        TextView ip = (TextView) view.findViewById(R.id.scan_port_target_ip);
        TextView start = (TextView) view.findViewById(R.id.scan_port_start);
        TextView end = (TextView) view.findViewById(R.id.scan_port_end);
        String ipAddr = ip.getText().toString();
        String startPort = start.getText().toString();
        String endPort = end.getText().toString();
        if (!InputValidate.isIpAddr(ipAddr)) {
            return;
        }
        if (startPort.equals("") && endPort.equals("")) {
            return;
        }
        if (startPort.length() >= 6 || endPort.length() >= 6) {
            return;
        }
        materialDialog.dismiss();
        actionInf.addScanTask(ipAddr, startPort, endPort);
    }


    @Override
    protected void onResume() {
        super.onResume();
        actionInf.recoveryUI();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.scan_port_toolbar);
        mToolbar.setTitleMarginStart(0);
        mToolbar.setTitle(AppConfig.TITLE_PORT);
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
            case GlobalEventT.scan_port_set_card_run_info_none:
                setCardNoInfo();
                break;
            case GlobalEventT.scan_port_set_card_run_info_new:
                setCardWaitScan((ScanPortState) event.obj);
                break;
            case GlobalEventT.scan_port_set_card_run_info_run:
                setCardRun((ScanPortState) event.obj);
                break;
            case GlobalEventT.scan_port_set_card_run_info_end:
                setCardWaitSave((ScanPortState) event.obj);
                break;
            case GlobalEventT.scan_port_recovery_radar_data:
                recoveryData((ScanPortResult) event.obj);
                break;
            case GlobalEventT.scan_port_add_radar_result:
                setRadarData((ScanPortResult) event.obj);
                break;
            case GlobalEventT.scan_port_refresh_radar_progress:
                setRadarProgress((ScanPortResult) event.obj);
                break;
            case GlobalEventT.scan_port_show_bottom_msg:
                showSnackMsgBox(event.tip);
                break;
        }
    }


    private void popMenu() {
        ScanPortState scanPortState = CacheProcess.getInstance().getScanPortState();
        TextView ip = (TextView) view.findViewById(R.id.scan_port_target_ip);
        TextView start = (TextView) view.findViewById(R.id.scan_port_start);
        TextView end = (TextView) view.findViewById(R.id.scan_port_end);
        ip.setText(scanPortState.targetIp);
        start.setText(scanPortState.startPort + "");
        end.setText(scanPortState.endPort + "");
        materialDialog.show();
    }

    private void setCardNoInfo() {

        lsScan.reset();
        TextView textTopTip = (TextView) findViewById(R.id.text_scan_port_tip);
        textTopTip.setVisibility(View.VISIBLE);
        RelativeLayout rlTopInfo = (RelativeLayout) findViewById(R.id.rl_scan_task_info);
        rlTopInfo.setVisibility(View.INVISIBLE);

        TextView textTaskInfo = (TextView) findViewById(R.id.text_scan_port_process_info);
        textTaskInfo.setText("");

        popTaskMenu.show();
    }

    private void setCardRun(ScanPortState scanPortState) {
        lsScan.startScan();
        TextView textTopTip = (TextView) findViewById(R.id.text_scan_port_tip);
        textTopTip.setVisibility(View.INVISIBLE);
        RelativeLayout rlTopInfo = (RelativeLayout) findViewById(R.id.rl_scan_task_info);
        rlTopInfo.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) findViewById(R.id.btn_scan_port_operator);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.stop_2));
        TextView textTarget = (TextView) findViewById(R.id.text_scan_port_ip);
        TextView textPorts = (TextView) findViewById(R.id.text_scan_port_ports);
        textTarget.setText("目标IP:" + scanPortState.targetIp);
        String ports = "目标端口:";
        ports += scanPortState.startPort + "-" + scanPortState.endPort;
        textPorts.setText(ports);
        popTaskMenu.hide();
    }

    private void setCardWaitScan(ScanPortState scanPortState) {

        lsScan.reset();
        RelativeLayout rlTopInfo = (RelativeLayout) findViewById(R.id.rl_scan_task_info);
        TextView textTopTip = (TextView) findViewById(R.id.text_scan_port_tip);
        rlTopInfo.setVisibility(View.VISIBLE);
        textTopTip.setVisibility(View.INVISIBLE);

        ImageView imageView = (ImageView) findViewById(R.id.btn_scan_port_operator);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.start_wifi));

        TextView textTarget = (TextView) findViewById(R.id.text_scan_port_ip);
        TextView textPorts = (TextView) findViewById(R.id.text_scan_port_ports);
        textTarget.setText("目标IP:" + scanPortState.targetIp);
        String ports = "目标端口:";
        ports += scanPortState.startPort + "-" + scanPortState.endPort;
        textPorts.setText(ports);

        popTaskMenu.show();
    }


    private void setCardWaitSave(ScanPortState scanPortState) {

        lsScan.stopScan();
        TextView textTopTip = (TextView) findViewById(R.id.text_scan_port_tip);
        textTopTip.setVisibility(View.INVISIBLE);
        RelativeLayout rlTopInfo = (RelativeLayout) findViewById(R.id.rl_scan_task_info);
        rlTopInfo.setVisibility(View.VISIBLE);

        ImageView imageView = (ImageView) findViewById(R.id.btn_scan_port_operator);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.save));

        TextView textTarget = (TextView) findViewById(R.id.text_scan_port_ip);
        TextView textPorts = (TextView) findViewById(R.id.text_scan_port_ports);
        textTarget.setText("目标IP:" + scanPortState.targetIp);
        String ports = "目标端口:";
        ports += scanPortState.startPort + "-" + scanPortState.endPort;
        textPorts.setText(ports);

        popTaskMenu.show();

    }


    private void recoveryData(final ScanPortResult scanPortResult) {
        Observable.from(scanPortResult.openPorts)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        lsScan.addText("[" + integer + "]", (integer % 5 + 2) * 1.00 / 7, integer * 360 / scanPortResult.maxCounts);
                    }
                });
        int percent = scanPortResult.currentScanIndex * 100 / scanPortResult.maxCounts;
        lsScan.setProgress(percent);
        refreshTaskInfo(scanPortResult);
    }

    private void setRadarData(ScanPortResult scanPortResult) {
        refreshTaskInfo(scanPortResult);
        int port = scanPortResult.openPorts.get(scanPortResult.openPorts.size() - 1);
        lsScan.addText("[" + port + "]", (port % 5 + 2) * 1.00 / 7, port * 360 / scanPortResult.maxCounts);
    }

    private void setRadarProgress(ScanPortResult scanPortResult) {
        int percent = scanPortResult.currentScanIndex * 100 / scanPortResult.maxCounts;
        lsScan.setProgress(percent);
        refreshTaskInfo(scanPortResult);
    }

    private void refreshTaskInfo(ScanPortResult scanPortResult) {
        TextView textTaskInfo = (TextView) findViewById(R.id.text_scan_port_process_info);
        textTaskInfo.setText("共扫描端口[" + scanPortResult.currentScanIndex + "]个，[" + scanPortResult.openPorts.size() + "]个端口处于开启状态!");
    }

    private void showSnackMsgBox(String tip) {
        RelativeLayout snack_container = (RelativeLayout) findViewById(R.id.scan_port_snack_container);
        LsSnack.show(this, snack_container, tip);
    }
}
