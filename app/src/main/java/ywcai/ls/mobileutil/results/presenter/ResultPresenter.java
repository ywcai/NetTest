package ywcai.ls.mobileutil.results.presenter;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.login.model.MyUser;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.presenter.inf.ResultPresenterInf;

public class ResultPresenter implements ResultPresenterInf {
    private ResultState resultState;//0代表不显示该类型数据，1代表显示该类型结果
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private List<LogIndex> remoteTemp = new ArrayList<>();

    public ResultPresenter() {
        //初始化默认筛选所有的数据
        resultState = cacheProcess.getResultState();
    }

    //点击标签过滤数据
    @Override
    public void refreshData() {
        if (resultState.isShowLocal) {
            reqLocalData();
        } else {
            pullDownForRemote();
        }
    }

    private void filterRemoteData() {
        Observable.from(remoteTemp)
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
            MyUser myUser = cacheProcess.getCacheUser();
            if (myUser == null) {
                sendMsResultTip("未登录，无法查看远端数据！");
                return;
            }
            ARouter.getInstance().build(AppConfig.DETAIL_ACTIVITY_REMOTE_PATH)
                    .withInt("pos", pos + 1)
                    .navigation();
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
        selectDataForTag();
    }

    @Override
    public void selectDataType(int typeIndex, boolean b) {
        resultState.isShow[typeIndex] = b ? 1 : 0;
        cacheProcess.setResultState(resultState);
        //点击当个数据时候，检测是否已经全选了数据
        boolean isSelectAll = isSelectAll();
        //更新全选按钮状态
        sendMsgRecoverySelectAllBtn(isSelectAll);
        selectDataForTag();//更新数据
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

    @Override
    public void pullDownForRemote() {
        reqRemoteData();
    }

    @Override
    public void selectDataForTag() {
        if (resultState.isShowLocal) {
            reqLocalData();
        } else {
            filterRemoteData();
        }
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


    //直接加载或者下拉
    private void reqRemoteData() {
        remoteTemp.clear();
        MyUser myUser = cacheProcess.getCacheUser();
        if (myUser == null) {
            Observable.just("")
                    .delay(200, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            updateDataList(remoteTemp);
                            sendMsResultTip("请先登录!");
                        }
                    });
            return;
        }
        HttpService httpService = RetrofitFactory.getHttpService();
        httpService.getLogList(myUser.userid)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<List<LogIndex>>() {
                    @Override
                    protected void success(List<LogIndex> logIndices) {
                        remoteTemp.addAll(logIndices);
                        filterRemoteData();
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        filterRemoteData();
                        sendMsResultTip("服务器拒绝，错误原因：" + msg.toString());

                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        filterRemoteData();
                        sendMsResultTip("网络连接失败：" + e.toString());
                    }
                });

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

    private void sendMsResultTip(String tip) {
        MsgHelper.sendEvent(GlobalEventT.result_remote_item_head, tip, null);
    }

}
