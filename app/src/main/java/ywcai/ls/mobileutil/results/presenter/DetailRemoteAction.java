package ywcai.ls.mobileutil.results.presenter;

import java.util.List;

import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.login.model.MyUser;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.presenter.inf.DetailActionInf;
import ywcai.ls.mobileutil.setting.LogEntity;

/**
 * Created by zmy_11 on 2018/2/9.
 */

public class DetailRemoteAction implements DetailActionInf {
    ResultState resultState = CacheProcess.getInstance().getResultState();
    HttpService httpService = RetrofitFactory.getHttpService();
    MyUser myUser = CacheProcess.getInstance().getCacheUser();
    private LogEntity myEntity = new LogEntity();


    @Override
    public void downloadRecordForRemote(final int pos) {
        if (myUser == null) {
            sendMsgToSnackBar("请先登录!", false);
            return;
        }
        if (pos == 0) {
            sendMsgToSnackBar("无效数据!", false);
            return;
        }
        httpService.delRecordForId(myUser.userid, myEntity.logIndex.recordId, pos, resultState)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<LogEntity>() {
                    @Override
                    protected void success(LogEntity logEntity) {
                        //先保存下载的实体数据，然后再更新当前缓存
                        saveEntityToLocal();
                        myEntity = logEntity;
                        if (myEntity.max <= 0) {
                            sendMsgToUpdateUi(0, null);
                            return;
                        }
                        //如果删除的是最后一页，则需要更新POS的位置。
                        if (pos > myEntity.max) {
                            sendMsgToUpdateUi(myEntity.max, myEntity);
                        } else {
                            sendMsgToUpdateUi(pos, myEntity);
                        }
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        sendMsgToSnackBar("服务端拒绝:" + msg, false);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        sendMsgToSnackBar("网络连接失败:" + e.toString(), false);
                    }
                });
    }

    private void saveEntityToLocal() {
        String data = myEntity.data;
        LogIndex logIndex = myEntity.logIndex;
        CacheProcess.getInstance().setCache(logIndex.cacheFileName, data);
        CacheProcess.getInstance().addCacheLogIndex(logIndex);
    }


    @Override
    public void shareRecordForRemote(int pos) {
        if (pos == 0) {
            sendMsgToSnackBar("无效数据!", false);
            return;
        }
    }

    @Override
    public void uploadRecord(int pos) {
        //远端数据不需实现
    }

    @Override
    public void deleteRecord(final int pos) {
        if (myUser == null) {
            sendMsgToSnackBar("请先登录!", false);
            return;
        }
        if (pos == 0) {
            sendMsgToSnackBar("无效数据!", false);
            return;
        }
        httpService.delRecordForId(myUser.userid, myEntity.logIndex.recordId, pos, resultState)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<LogEntity>() {
                    @Override
                    protected void success(LogEntity logEntity) {
                        //若删除成功则需要更新result页面的LIST.
                        myEntity = logEntity;
                        if (logEntity.max <= 0) {
                            sendMsgToUpdateUi(0, null);
                            return;
                        }
                        //如果删除的是最后一页，则需要更新POS的位置。
                        if (pos > myEntity.max) {
                            sendMsgToUpdateUi(myEntity.max, myEntity);
                        } else {
                            sendMsgToUpdateUi(pos, myEntity);
                        }
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        sendMsgToSnackBar("服务端拒绝:" + msg, false);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        sendMsgToSnackBar("网络连接失败:" + e.toString(), false);
                    }
                });
    }

    @Override
    public void editRecordTitle(final int pos, final String titleName) {
        if (myUser == null) {
            sendMsgToUpdateUi(pos, null);
            sendMsgToSnackBar("请先登录!", false);
            return;
        }
        if (pos == 0) {
            sendMsgToSnackBar("无效数据!", false);
            return;
        }
        httpService.editRecordName(myUser.userid, myEntity.logIndex.recordId, titleName)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<LogEntity>() {
                    @Override
                    protected void success(LogEntity logEntity) {
                        myEntity = logEntity;
                        sendMsgToUpdateNickName(titleName);
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        sendMsgToSnackBar("服务端拒绝:" + msg, false);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        sendMsgToSnackBar("网络连接失败:" + e.toString(), false);
                    }
                });
    }


    @Override
    public void loadRecord(final int currentPos) {
        if (myUser == null) {
            sendMsgToUpdateUi(currentPos, null);
            sendMsgToSnackBar("请先登录!", false);
            return;
        }
        if (currentPos == 0) {
            sendMsgToSnackBar("无效数据!", false);
            return;
        }
        httpService.loadRemoteDetailRecord(myUser.userid, currentPos, resultState)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<LogEntity>() {
                    @Override
                    protected void success(LogEntity logEntity) {
                        myEntity = logEntity;
                        sendMsgToUpdateUi(currentPos, logEntity);
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        sendMsgToUpdateUi(currentPos, null);
                        sendMsgToSnackBar("服务端拒绝:" + msg, false);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        sendMsgToUpdateUi(currentPos, null);
                        sendMsgToSnackBar("网络连接失败:" + e.toString(), false);
                    }
                });
    }

    @Override
    public void prev(int currentPos) {
        if (currentPos == 1) {
            return;
        }
        loadRecord(currentPos - 1);
    }

    @Override
    public void next(int currentPos) {
        loadRecord(currentPos + 1);
    }


    private void sendMsgToUpdateUi(int currentPos, LogEntity logEntity) {
        MsgHelper.sendEvent(GlobalEventT.detail_remote_refresh_record, currentPos + "", logEntity);
    }

    //同时关闭加载窗口
    private void sendMsgToSnackBar(String msg, boolean success) {
        MsgHelper.sendEvent(GlobalEventT.global_pop_snack_tip, msg, success);
    }

    private void sendMsgToUpdateNickName(String titleName) {
        MsgHelper.sendEvent(GlobalEventT.detail_remote_refresh_aliasname, titleName, null);
    }
}
