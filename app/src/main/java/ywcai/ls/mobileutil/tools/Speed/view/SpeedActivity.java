package ywcai.ls.mobileutil.tools.Speed.view;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mobstat.StatService;
import com.daimajia.numberprogressbar.NumberProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.drakeet.materialdialog.MaterialDialog;
import ywcai.ls.control.LoadingDialog;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.tools.Speed.presenter.SpeedAction;
import ywcai.ls.mobileutil.tools.Speed.presenter.inf.SpeedActionInf;

@Route(path = "/tools/Speed/view/SpeedActivity")
public class SpeedActivity extends AppCompatActivity {
    SpeedActionInf speedActioninf;
    YibiaoView yibiaoView;
    RelativeLayout rl;
    MaterialDialog popSub;
    MaterialDialog operatorMenu;
    View view;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        setContentView(R.layout.activity_speed);
        initAction();
        intToolBar();
        initView();
    }

    private void intToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.speed_toolbar);
        mToolbar.setTitleMarginStart(0);
        mToolbar.setTitle(AppConfig.TITLE_SPEED);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        popSub = new MaterialDialog(this);
        popSub.setCanceledOnTouchOutside(true);
        TextView textView = new TextView(this);
        textView.setText("预计将消耗50~100M流量\n是否继续？");
        textView.setMaxLines(3);
        textView.setTextSize(14);
        textView.setTextColor(Color.DKGRAY);
        popSub.setContentView(textView);
        popSub.setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSub.dismiss();
            }
        });
        popSub.setNegativeButton("继续", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSub.dismiss();
                speedActioninf.clickStartBtn();
            }
        });
        view = LayoutInflater.from(this).inflate(R.layout.pop_dialog_speed, null);
        operatorMenu = new MaterialDialog(this);
        operatorMenu.setContentView(view);
        operatorMenu.setCanceledOnTouchOutside(false);
        Button localSave = (Button) view.findViewById(R.id.speed_save_local);
        Button remoteSave = (Button) view.findViewById(R.id.speed_save_remote);
        Button resetResult = (Button) view.findViewById(R.id.speed_clear);
        Button cancal = (Button) view.findViewById(R.id.speed_cancal);
        localSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedActioninf.clickSaveForLocal();
                operatorMenu.dismiss();
            }
        });
        remoteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedActioninf.clickSaveForRemote();
                operatorMenu.dismiss();
            }
        });
        resetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedActioninf.clickReset();
                operatorMenu.dismiss();
            }
        });
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedActioninf.cancalOperator();
                operatorMenu.dismiss();
            }
        });

        rl = (RelativeLayout) findViewById(R.id.speed_container);
        yibiaoView = new YibiaoView(this);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setMargins(0, 0, 0, 0);
        yibiaoView.setLayoutParams(layout);
        yibiaoView.setPadding(0, 0, 0, 0);
        rl.addView(yibiaoView, 0);
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.speed_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSub.show();
            }
        });
        FloatingActionButton btn_operator = (FloatingActionButton) findViewById(R.id.speed_operator);
        btn_operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popOperator();
                speedActioninf.clickPopMenu();
            }
        });
    }

    private void initAction() {
        speedActioninf = new SpeedAction();
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
    protected void onPause() {
        StatService.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        StatService.onResume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        speedActioninf.closeActivity();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateSpeed(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.speed_test_info:
                showTip(event.tip);
                break;
            case GlobalEventT.speed_yibiao_read_data:
                setSpeed((float) event.obj);
                break;
            case GlobalEventT.speed_set_progress:
                setLsProgress((int) event.obj);
                break;
            case GlobalEventT.speed_set_ready:
                initReady();
                break;
            case GlobalEventT.speed_set_running_receive_task:
                loadTask();
                break;
            case GlobalEventT.speed_set_complete:
                completeTask(event.tip);
                break;
            case GlobalEventT.global_pop_snack_tip:
                closeLoading();
                showSnackBar(event.tip, (boolean) event.obj);
                break;
            case GlobalEventT.global_pop_loading_dialog:
                popLoading(event.tip);
                break;
        }
    }


    private void initReady() {
        setSpeed(0.00f);
        setLsProgress(0);
        setStartBtnVisible(true);
        setPopMenuVisible(false);
        showTip("");
    }


    private void loadTask() {
        setStartBtnVisible(false);
        setProgressVisible(true);
    }


    private void completeTask(String tip) {
        setProgressVisible(false);
        setPopMenuVisible(true);
        showTip(tip);
    }

    private void popOperator() {
        operatorMenu.show();
    }

    private void setPopMenuVisible(boolean b) {
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.speed_operator);
        if (b) {
            btn.show();
        } else {
            btn.hide();
        }
    }


    private void setProgressVisible(boolean visible) {
        NumberProgressBar progressBar = (NumberProgressBar) findViewById(R.id.speed_progress);
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setStartBtnVisible(boolean visible) {
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.speed_start);
        if (visible) {
            btn.show();
        } else {
            btn.hide();
        }
    }

    private void setLsProgress(int per) {
        NumberProgressBar progressBar = (NumberProgressBar) findViewById(R.id.speed_progress);
        progressBar.setProgress(per);
    }

    private void setSpeed(float speed) {
        yibiaoView.setRealTimeValue(speed);
    }

    private void showTip(String tip) {
        TextView text = (TextView) findViewById(R.id.speed_tip);
        text.setText(tip);
    }

    private void showSnackBar(String tip, boolean success) {
        RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.speed_snack_bar);
        if (success) {
            LsSnack.show(this, rl_container, tip);
        } else {
            LsSnack.show(this, rl_container, tip, R.color.LRed);
        }
    }

    private void closeLoading() {
        loadingDialog.dismiss();
    }

    private void popLoading(String tip) {
        loadingDialog.setLoadingText(tip);
        loadingDialog.show();
    }


}
