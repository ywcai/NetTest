package ywcai.ls.mobileutil.results.presenter;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsListTransfer;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.http.UploadResult;
import ywcai.ls.mobileutil.login.model.MyUser;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.presenter.inf.DetailActionInf;
import ywcai.ls.mobileutil.setting.LogEntity;


public class DetailLocalAction implements DetailActionInf {
    CacheProcess cacheProcess = CacheProcess.getInstance();
    ResultState resultState;
    List<LogIndex> temp = null;
    List<LogIndex> all = cacheProcess.getCacheLogIndex();
    private LogIndex currentLog;
    HttpService httpService = RetrofitFactory.getHttpService();

    public DetailLocalAction(final ResultState resultState, List<LogIndex> _temp) {
        this.resultState = resultState;
        this.temp = _temp;
        updateTempList();
    }

    private void updateTempList() {
        Observable.from(all)
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
                        temp.clear();
                        temp.addAll(logIndices);
                    }
                });
    }

    private int getRealPos() {
        int realPos = LsListTransfer.getIndexWithLogIndex(all, currentLog);
        return realPos;
    }

    @Override
    public void downloadRecordForRemote(int pos) {
        //不相关的方法
    }

    @Override
    public void shareRecordForRemote(int pos) {
        //不相关的方法
    }

    @Override
    public void uploadRecord(final int pos) {
        if (pos < 0) {
            sendMsgSnack("当前无数据", false);
            return;
        }
        MyUser myUser = CacheProcess.getInstance().getCacheUser();
        if (myUser == null) {
            sendMsgSnack("上传数据请先登录APP", false);
            return;
        }
        LogEntity logEntity = new LogEntity();
        logEntity.logIndex = currentLog;
        String payload = CacheProcess.getInstance().getCache(currentLog.cacheFileName);
        if (payload == null) {
            logEntity.data = "null";
        } else {
            logEntity.data = payload;
        }
        httpService.addRecord(myUser.userid, logEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<UploadResult>() {
                    @Override
                    protected void success(UploadResult uploadResult) {
                        int realPos = getRealPos();
                        temp.remove(pos);
                        try {
                            all.remove(realPos);
                        } catch (Exception e) {

                        }
                        cacheProcess.setCacheLogIndex(all);
                        int currentPos = -1;
                        if (temp.size() == 0) {
                            currentLog = null;
                        } else {
                            currentPos = temp.size() - 1 >= pos ? pos : temp.size() - 1;
                            currentLog = temp.get(currentPos);
                        }
                        sendMsgSnack("上传成功", true);
                        sendMsgLoadLog(currentPos);
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        sendMsgSnack("上传失败:" + msg, false);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        sendMsgSnack("网络连接失败:" + e.toString(), false);
                    }
                });
    }


    @Override
    public void deleteRecord(final int pos) {
        if (pos < 0) {
            sendMsgSnack("当前无数据", false);
            return;
        }
        Observable.just(pos)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        int realPos = getRealPos();
                        temp.remove(pos);
                        try {
                            all.remove(realPos);
                        } catch (Exception e) {

                        }
                        cacheProcess.setCacheLogIndex(all);
                        int currentPos = -1;
                        if (temp.size() == 0) {
                            currentLog = null;
                        } else {
                            currentPos = temp.size() - 1 >= pos ? pos : temp.size() - 1;
                            currentLog = temp.get(currentPos);
                        }
                        sendMsgLoadLog(currentPos);
                    }
                });
    }

    @Override
    public void editRecordTitle(final int pos, final String titleName) {
        if (pos < 0) {
            sendMsgSnack("当前无数据", false);
            return;
        }
        Observable.just(pos)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        int realPos = getRealPos();
                        currentLog.aliasFileName = titleName;
                        all.remove(realPos);
                        all.add(realPos, currentLog);
                        cacheProcess.setCacheLogIndex(all);
                        sendMsgLoadLog(pos);
                    }
                });
    }

    @Override
    public void loadRecord(int currentPos) {
        Observable.just(currentPos)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer currentPos) {
                        try {
                            currentLog = temp.get(currentPos);
                        } catch (Exception e) {
                            currentLog = null;
                            sendMsgSnack("没有数据，数据可能已经被删除", false);
                        }
                        sendMsgLoadLog(currentPos);
                    }
                });
    }

    @Override
    public void prev(int currentPos) {
        currentPos = currentPos - 1;
        loadRecord(currentPos);
    }

    @Override
    public void next(int currentPos) {
        currentPos = currentPos + 1;
        loadRecord(currentPos);
    }

    private void sendMsgLoadLog(int currentPos) {
        MsgHelper.sendStickEvent(GlobalEventT.detail_local_refresh_record, currentPos + "", currentLog);
    }

    private void sendMsgSnack(String tip, boolean success) {
        MsgHelper.sendStickEvent(GlobalEventT.global_pop_snack_tip, tip, success);
    }
}
