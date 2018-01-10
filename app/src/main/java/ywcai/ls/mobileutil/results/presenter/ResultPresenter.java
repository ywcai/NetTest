package ywcai.ls.mobileutil.results.presenter;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.presenter.inf.ResultPresenterInf;

public class ResultPresenter implements ResultPresenterInf {
    private ResultState resultState;//0代表不显示该类型数据，1代表显示该类型结果
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private int nowPage = 0, maxPage, pageSize = 20;//如果是远端数据，当前加载的页码

    public ResultPresenter() {
        //初始化默认筛选所有的数据
        resultState = cacheProcess.getResultState();
    }

    @Override
    public void refreshData() {
        if (resultState.isShowLocal) {
            reqLocalData();
        } else {
            reqRemoteData();
        }
    }

    @Override
    public void pressLocalBtn() {
        resultState.isShowLocal = true;
        cacheProcess.setResultState(resultState);
        reqLocalData();
    }

    @Override
    public void pressRemoteBtn() {
        resultState.isShowLocal = false;
        cacheProcess.setResultState(resultState);
        reqRemoteData();
    }

    @Override
    public void onClickCard(int pos) {
        if (resultState.isShowLocal) {
            ARouter.getInstance().build(AppConfig.DETAIL_ACTIVITY_PATH).withInt("pos", pos).navigation();
        } else {

        }

    }

    @Override
    public void selectDataTypeAll() {
        //检测当前数据选择情况
        boolean isSelectAll = isSelectAll();
        //先将所有数据至未0;
        resultState.isShow = new int[AppConfig.getMenuTextStr().length];
        //如果当前没有选择所有，则选择所有
        if (!isSelectAll) {
            for (int i = 0; i < resultState.isShow.length; i++) {
                resultState.isShow[i] = 1;
            }
        }
        isSelectAll = !isSelectAll;
        cacheProcess.setResultState(resultState);
        //切换全选按钮属性
        sendMsgRecoverySelectAllBtn(isSelectAll);
        //重新设置TAG的选择状态
        recoveryTag();
        //刷新数据
        refreshData();
    }

    @Override
    public void selectDataType(int typeIndex, boolean b) {
        resultState.isShow[typeIndex] = b ? 1 : 0;
        cacheProcess.setResultState(resultState);
        //点击当个数据时候，检测是否已经全选了数据
        boolean isSelectAll = isSelectAll();
        //更新全选按钮状态
        sendMsgRecoverySelectAllBtn(isSelectAll);
        refreshData();//更新数据
    }

    private boolean isSelectAll() {
        boolean isSelectAll = false;
        //响应外部按钮点击事件
        for (int i = 0; i < resultState.isShow.length; i++) {
            if (resultState.isShow[i] == 0) {
                isSelectAll = false;
                break;
            }
            isSelectAll = true;
        }
        return isSelectAll;
    }


    @Override
    public void initTagStatus() {
        recoveryTag();
        //更新全选按钮状态
        boolean isSelectAll = isSelectAll();
        sendMsgRecoverySelectAllBtn(isSelectAll);
    }


    private void reqLocalData() {
        List<LogIndex> temp = CacheProcess.getInstance().getCacheLogIndex();
        if (temp == null) {
            temp = new ArrayList<>();
        }
        Observable.from(temp)
                .filter(new Func1<LogIndex, Boolean>() {
                    @Override
                    public Boolean call(LogIndex logIndex) {

                        return resultState.isShow[logIndex.cacheTypeIndex] == 1 ? true : false;
                    }
                })
                .toList()
                .subscribe(new Action1<List<LogIndex>>() {
                    @Override
                    public void call(List<LogIndex> logIndices) {
                        updateDataList(logIndices);
                    }
                });
    }

    private void reqRemoteData() {
        //refresh
        updateDataList(new ArrayList<LogIndex>());
    }

    private void updateDataList(List<LogIndex> showList) {
        MsgHelper.sendEvent(GlobalEventT.result_update_list, "", showList);
    }


    private void recoveryTag() {
        MsgHelper.sendEvent(GlobalEventT.result_update_tag_status, "", resultState);
    }

    private void sendMsgRecoverySelectAllBtn(boolean isSelectAll) {
        MsgHelper.sendEvent(GlobalEventT.result_update_top_btn_status, "", isSelectAll);
    }

}
