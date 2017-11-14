package ywcai.ls.mobileutil.results.presenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
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
    public void deleteData(int[] logIndex) {
        if (resultState.isShowLocal) {
            deleteLocalData(logIndex);
        } else {
            deleteRemoteData(logIndex);
        }
    }

    @Override
    public void onClickCard(int pos) {

    }

    @Override
    public void selectDataTypeAll() {
        Observable.from(resultState.isShow)
                .all(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer == 1;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            //全选
                            resultState.isShow = new Integer[]{
                                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

                        } else

                        {
                            resultState.isShow = new Integer[]{
                                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                        }
                        cacheProcess.setResultState(resultState);
                        recoveryTag();
                        refreshData();
                    }
                });
    }

    @Override
    public void selectDataType(int typeIndex) {
        resultState.isShow[typeIndex] = resultState.isShow[typeIndex] == 1 ? 0 : 1;
        cacheProcess.setResultState(resultState);
        recoveryTag();
        refreshData();
    }
    @Override
    public void initTagStatus()
    {
        recoveryTag();
    }



    private void reqLocalData() {
        List<LogIndex> temp = CacheProcess.getInstance().getCacheLogIndex();
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

    private void deleteLocalData(int[] pos) {

    }

    private void deleteRemoteData(int[] pos) {

    }


    private void updateDataList(List<LogIndex> showList) {
        MsgHelper.sendEvent(GlobalEventT.result_update_list, "", showList);
    }


    private void recoveryTag() {
        MsgHelper.sendEvent(GlobalEventT.result_update_tag_status, "", resultState);
    }
}
