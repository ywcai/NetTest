package ywcai.ls.mobileutil.results.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsListTransfer;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.presenter.inf.DetailLocalActionInf;


public class DetailLocalAction implements DetailLocalActionInf {
    CacheProcess cacheProcess = CacheProcess.getInstance();
    ResultState resultState;
    List<LogIndex> temp = null;
    List<LogIndex> all = cacheProcess.getCacheLogIndex();

    public DetailLocalAction(final ResultState resultState, List<LogIndex> temp) {
        this.resultState = resultState;
        this.temp = temp;
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
                        temp.addAll(logIndices);
                    }
                });
    }

    private int getRealPos(int tempPos) {
        LogIndex logIndex = temp.get(tempPos);
        int realPos = LsListTransfer.getIndexWithLogIndex(all, logIndex);
        return realPos;
    }


    @Override
    public void uploadRecord(final int pos) {
        if (pos < 0) {
            sendMsgSnack("当前已无数据", false);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean upload = requestUpload();
                if (upload) {
                    int realPos = getRealPos(pos);
                    temp.remove(pos);
                    all.remove(realPos);
                    cacheProcess.setCacheLogIndex(all);
                    int currentPos = temp.size() - 1 >= pos ? pos : temp.size() - 1;
                    sendMsgSnack("上传成功", true);
                    loadRecord(currentPos);
                } else {
                    sendMsgSnack(AppConfig.LOG_REMOTE_SAVE_SUCCESS, false);
                }
            }
        }).start();
    }

    private boolean requestUpload() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void deleteRecord(final int pos) {
        if (pos < 0) {
            sendMsgSnack("当前已无数据", false);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int realPos = getRealPos(pos);
                temp.remove(pos);
                all.remove(realPos);
                cacheProcess.setCacheLogIndex(all);
                int currentPos = temp.size() - 1 >= pos ? pos : temp.size() - 1;
                sendMsgSnack("删除成功", true);
                loadRecord(currentPos);
            }
        }).start();
    }

    @Override
    public void editRecordTitle(final int pos, final String titleName) {
        if (pos < 0) {
            sendMsgSnack("当前已无数据", false);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogIndex currentLogIndex = temp.get(pos);
                int realPos = getRealPos(pos);
                currentLogIndex.aliasFileName = titleName;
                all.remove(realPos);
                all.add(realPos, currentLogIndex);
                cacheProcess.setCacheLogIndex(all);
                loadRecord(pos);
            }
        }).start();
    }

    @Override
    public void loadRecord(final int currentPos) {
        LogIndex logIndex = null;
        if (currentPos >= 0 && currentPos < temp.size()) {
            logIndex = temp.get(currentPos);
        }
        final LogIndex finalLogIndex = logIndex;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendMsgLoadLog(currentPos, finalLogIndex);
            }
        }).start();
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

    private void sendMsgLoadLog(int currentPos, LogIndex logIndex) {
        MsgHelper.sendStickEvent(GlobalEventT.detail_local_refresh_record, currentPos + "", logIndex);
    }

    private void sendMsgSnack(String tip, boolean success) {
        MsgHelper.sendStickEvent(GlobalEventT.global_pop_snack_tip, tip, success);
    }
}
