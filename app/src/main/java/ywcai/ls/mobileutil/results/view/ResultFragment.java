package ywcai.ls.mobileutil.results.view;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import mehdi.sakout.fancybuttons.FancyButton;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultAdapter;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.presenter.ResultPresenter;
import ywcai.ls.mobileutil.results.presenter.inf.ResultPresenterInf;

@Route(path = "/main/resultFragment")
public class ResultFragment extends Fragment {
    private RecyclerView resultRecyclerView;
    private View view;
    private ResultPresenterInf resultPresenterInf = new ResultPresenter();
    private List<LogIndex> listCurrent;
    private ResultAdapter resultAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        resultPresenterInf.refreshData();
        resultPresenterInf.initTagStatus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main_result, container, false);
        //安装网格化选择菜单
        InitImage();
        InitBtn();
        addTagClickListener();
        initDataList();
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
        final FancyButton btnLocal = (FancyButton) view.findViewById(R.id.select_local);
        final FancyButton btnRemote = (FancyButton) view.findViewById(R.id.select_remote);
        final FancyButton btnType = (FancyButton) view.findViewById(R.id.select_test_type);
        btnLocal.setEnabled(false);
        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultPresenterInf.pressLocalBtn();
                btnRemote.setEnabled(true);
                btnLocal.setEnabled(false);

            }
        });
        btnRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultPresenterInf.pressRemoteBtn();
                btnRemote.setEnabled(false);
                btnLocal.setEnabled(true);
            }
        });
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultPresenterInf.selectDataTypeAll();
            }
        });
    }

    private void initDataList() {
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
        final FlexboxLayout flexboxLayout = (FlexboxLayout) view.findViewById(R.id.flowLayout);
        final FancyButton btnType = (FancyButton) view.findViewById(R.id.select_test_type);
        String[] menuTextStr = AppConfig.getMenuTextStr();
        int flag = 0;
        for (int i = 0; i < menuTextStr.length; i++) {
            FancyButton tag = (FancyButton) flexboxLayout.getChildAt(i);
            tag.setGhost(status.isShow[i] == 0 ? true : false);
            flag += status.isShow[i];
        }
        if (flag >= menuTextStr.length) {
            btnType.setText("取消");
            btnType.setGhost(false);
        } else {
            btnType.setText("全选");
            btnType.setGhost(true);
        }
    }

    private void addTagClickListener() {
        final FlexboxLayout flexboxLayout = (FlexboxLayout) view.findViewById(R.id.flowLayout);
        String[] menuTextStr = AppConfig.getMenuTextStr();
        for (int i = 0; i < menuTextStr.length; i++) {
            FancyButton tag = (FancyButton) flexboxLayout.getChildAt(i);
            tag.setOnClickListener(new TagClickListener(i));
            tag.setText(menuTextStr[i]);
        }
    }

    class TagClickListener implements View.OnClickListener {
        private int pos;

        public TagClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            resultPresenterInf.selectDataType(pos);
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDeviceList(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.result_update_list:
                updateRecordList((List<LogIndex>) event.obj);
                break;
            case GlobalEventT.result_update_tag_status:
                recoveryTag((ResultState)event.obj);
                break;

        }
    }

    private void updateRecordList(List<LogIndex> obj) {
        listCurrent.clear();
        listCurrent.addAll(obj);
        resultAdapter.notifyDataSetChanged();
        TextView totalNum = (TextView) view.findViewById(R.id.result_text_tip);
        totalNum.setText(listCurrent.size() + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
