package ywcai.ls.mobileutil.results.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.BlurTransformation;
import mehdi.sakout.fancybuttons.FancyButton;
import rx.Observable;
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
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultAdapter;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.presenter.ResultPresenter;
import ywcai.ls.mobileutil.results.presenter.inf.ResultPresenterInf;
import ywcai.ls.smooth.tip.SmoothTip;

@Route(path = "/menu_detail_local/resultFragment")
public class ResultFragment extends Fragment {
    private RecyclerView resultRecyclerView;
    private View view;
    private ResultPresenterInf resultPresenterInf = new ResultPresenter();
    private List<LogIndex> listCurrent;
    private ResultAdapter resultAdapter;
    private FlexButtonLayout flexButtonLayout;
    private FancyButton btnType, btnLocal, btnRemote;
    SwipeRefreshLayout swl;
    SmoothTip resultTip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void InitSwl() {
        resultTip = (SmoothTip) view.findViewById(R.id.result_tip);
        swl = (SwipeRefreshLayout) view.findViewById(R.id.result_refresh);
        swl.setEnabled(true);
        swl.setProgressViewOffset(false, 0, 130);
        swl.setColorSchemeResources(R.color.LOrange);
        swl.setProgressBackgroundColorSchemeResource(R.color.card_background);
        swl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resultPresenterInf.pullDown();
            }
        });
    }

    private void showToast(String tip) {
        Toast.makeText(this.getContext(), tip, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        swl.setRefreshing(true);
        //从缓存初始化恢复Tag选择状态
        resultPresenterInf.initTagStatus();
        //自动刷新数据
        resultPresenterInf.pullDown();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main_result, container, false);
        //安装网格化选择菜单
        InitImage();
        InitBtn();
        InitSwl();
        createTag();
        createListItem();
        setTitleText();
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


    private void InitImage() {
        Glide.with(this).load(R.drawable
                .main_menu_top)
                .bitmapTransform(new BlurTransformation(this.getContext(), 10))
                .into((ImageView) view.findViewById(R.id.main_head_bg_result));
    }

    private void InitBtn() {
        btnLocal = (FancyButton) view.findViewById(R.id.select_local);
        btnRemote = (FancyButton) view.findViewById(R.id.select_remote);
        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swl.setRefreshing(true);
                btnRemote.setEnabled(true);
                btnLocal.setEnabled(false);
                resultPresenterInf.pressLocalBtn();
            }
        });
        btnRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swl.setRefreshing(true);
                btnRemote.setEnabled(false);
                btnLocal.setEnabled(true);
                resultPresenterInf.pressRemoteBtn();
            }
        });

        btnType = (FancyButton) view.findViewById(R.id.select_test_type);
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultPresenterInf.selectDataTypeAll();
            }
        });
    }

    private void setTitleText() {
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_result);
        toolbarLayout.setTitleEnabled(true);
        toolbarLayout.setEnabled(true);
        toolbarLayout.setTitle("");
        toolbarLayout.setCollapsedTitleGravity(Gravity.RIGHT);
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.result_toolbar_1);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    }

    private void createTag() {
        flexButtonLayout = (FlexButtonLayout) view.findViewById(R.id.flowLayout);
        String[] menuTextStr = AppConfig.getMenuTextStr();
        List<String> tagStrings = new ArrayList<>();
        for (int i = 0; i < menuTextStr.length; i++) {
            tagStrings.add(menuTextStr[i]);
        }
        flexButtonLayout.setDataAdapter(tagStrings);
        flexButtonLayout.setOnFlexButtonClickListener(new OnFlexButtonClickListener() {
            @Override
            public void clickItem(int i, boolean b) {
                resultPresenterInf.selectDataType(i, b);
            }

            @Override
            public void clickAllBtn(int[] ints, boolean b) {
                //这里在外部构建了全选按钮，因此不做处理
            }
        });
    }

    private void createListItem() {
        resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_list);
        listCurrent = new ArrayList<>();
        resultAdapter = new ResultAdapter(this.getContext(), listCurrent);
        resultRecyclerView.setAdapter(resultAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(false);
        layoutManager.setReverseLayout(false);
        resultRecyclerView.setLayoutManager(layoutManager);
        resultAdapter.setOnclickListener(new OnItemClickListener() {
            @Override
            public void OnClickItem(View v, int pos) {
                resultPresenterInf.onClickCard(pos);
            }
        });
    }

    private void recoveryTag(ResultState status) {
        flexButtonLayout.setSelectIndex(status.isShow);
        if (status.isShowLocal) {
            btnLocal.setEnabled(false);
            btnRemote.setEnabled(true);
        } else {
            btnLocal.setEnabled(true);
            btnRemote.setEnabled(false);
        }
    }

    private void setSelectAllBtnStatus(boolean isCurrentSelectAll) {
        if (isCurrentSelectAll) {
            btnType.setText("取消");
            btnType.setGhost(false);
        } else {
            btnType.setText("全选");
            btnType.setGhost(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateDeviceList(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.result_update_list:
                updateRecordList((List<LogIndex>) event.obj);
                break;
            case GlobalEventT.result_update_tag_status:
                recoveryTag((ResultState) event.obj);
                break;
            case GlobalEventT.result_update_top_btn_status:
                setSelectAllBtnStatus((boolean) event.obj);
                break;
            case GlobalEventT.result_start_pull_refresh:
                refreshData();
                break;
            case GlobalEventT.result_remote_item_head:
                resultTip.showTip(event.tip, 1000, SmoothTip.SMOOTH_ORIENTATION_TOP);
                break;
        }
    }

    private void updateRecordList(List<LogIndex> obj) {
        listCurrent.clear();
        listCurrent.addAll(obj);
        resultAdapter.notifyDataSetChanged();
        TextView totalNum = (TextView) view.findViewById(R.id.result_text_tip);
        totalNum.setText(listCurrent.size() + "");
        swl.setRefreshing(false);
    }
}
