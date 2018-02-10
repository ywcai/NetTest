package ywcai.ls.mobileutil.setting;

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
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.http.UploadResult;
import ywcai.ls.mobileutil.login.model.MyUser;
import ywcai.ls.mobileutil.results.model.LogIndex;

public class SetAction implements SetActionInf {
    CacheProcess cacheProcess = CacheProcess.getInstance();
    HttpService service = RetrofitFactory.getHttpService();
    @Override
    public void clearRecord() {
        List<LogIndex> logs = cacheProcess.getCacheLogIndex();
        Observable.from(logs)
                .flatMap(new Func1<LogIndex, Observable<LogIndex>>() {
                    @Override
                    public Observable<LogIndex> call(LogIndex logIndex) {
                        cacheProcess.deleteCache(logIndex.cacheFileName);
                        return Observable.just(logIndex);
                    }
                })
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<LogIndex>() {
                    @Override
                    public void call(LogIndex logIndex) {
                        cacheProcess.setCacheLogIndex(null);
                        sendMsgToCloseLoadDialog();
                        sendMsgToClearResultActivityRecord();
                        sendMsgToToast("清除记录完成");
                    }
                });
    }

    @Override
    public void updateRecord(final MyUser myUser) {
        final List list = new ArrayList<>();
        Observable.from(cacheProcess.getCacheLogIndex())
                .flatMap(new Func1<LogIndex, Observable<LogEntity>>() {
                    @Override
                    public Observable<LogEntity> call(LogIndex logIndex) {
                        LogEntity logEntity = new LogEntity();
                        logEntity.logIndex = logIndex;
                        logEntity.data = cacheProcess.getCache(logIndex.cacheFileName);
                        return Observable.just(logEntity);
                    }
                })
                .toList()
                .subscribe(new Action1<List<LogEntity>>() {
                    @Override
                    public void call(List<LogEntity> logEntities) {
                        list.addAll(logEntities);
                    }
                });
        service.postRecords(myUser.userid, list)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        new BaseObserver<UploadResult>() {
                            @Override
                            protected void success(UploadResult result) {
                                clearRecord();
                                sendMsgToCloseLoadDialog();
                                sendMsgToToast( "上传完成");
                            }

                            @Override
                            protected void onCodeError(int code, String msg) {
                                sendMsgToCloseLoadDialog();
                                sendMsgToToast("服务端拒绝，错误原因："+msg);
                            }

                            @Override
                            protected void onNetError(Throwable e) {
                                sendMsgToCloseLoadDialog();
                                sendMsgToToast("网络错误："+e.toString());
                            }
                        }
                );
    }

    @Override
    public void editNickName(MyUser myUser, String nickname) {
        //请求到服务端更新;
        service.postNickname(myUser.userid, nickname)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<MyUser>() {
                    @Override
                    protected void success(MyUser myUser) {
                        cacheProcess.setCacheUser(myUser);
                        sendMsgForQQLoginEnd(myUser);
                        sendMsgToCloseLoadDialog();
                        sendMsgToToast("修改昵称完成!");
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        sendMsgToCloseLoadDialog();
                        sendMsgToToast("服务器拒绝，错误原因: "+msg);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        sendMsgToCloseLoadDialog();
                        sendMsgToToast("网络错误: "+e.toString());
                    }
                });

    }

    @Override
    public void popHelpMsg() {
        ARouter.getInstance().build(AppConfig.WEB_ACTIVITY_PATH)
                .withString("httpUrl","http://119.6.204.54:8080/NetTest/open/help")
                .withString("title","使用说明")
                .navigation();
    }

    @Override
    public void popContract() {
        ARouter.getInstance().build(AppConfig.WEB_ACTIVITY_PATH)
                .withString("httpUrl","http://119.6.204.54:8080/NetTest/open/us")
                .withString("title","关于我们")
                .navigation();
    }

    @Override
    public void loginOut() {
        Observable.just("")
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        cacheProcess.setCacheUser(null);
                        sendMsgToCloseLoadDialog();
                        sendMsgForQQLoginEnd(null);
                    }
                });
    }

    private void sendMsgToToast(String msg) {
        MsgHelper.sendEvent(GlobalEventT.setting_pop_toast_tip, msg, null);
    }

    private void sendMsgToCloseLoadDialog() {
        MsgHelper.sendStickEvent(GlobalEventT.setting_dialog_close, "", null);
    }

    private void sendMsgToClearResultActivityRecord() {
        MsgHelper.sendStickEvent(GlobalEventT.result_local_clear_record, "", null);
    }

    private void sendMsgForQQLoginEnd(MyUser myUser) {
        MsgHelper.sendStickEvent(GlobalEventT.setting_qq_login_success, "", myUser);
    }

}
