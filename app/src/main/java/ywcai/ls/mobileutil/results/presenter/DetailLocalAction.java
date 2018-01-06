package ywcai.ls.mobileutil.results.presenter;

import java.util.List;
import java.util.Random;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.presenter.inf.DetailLocalActionInf;


public class DetailLocalAction implements DetailLocalActionInf {
    CacheProcess cacheProcess = CacheProcess.getInstance();

    @Override
    public void uploadRecord(final int pos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean upload = requestUpload();
                if (upload) {
                    List<LogIndex> list = cacheProcess.getCacheLogIndex();
                    list.remove(pos);
                    int currentPos = list.size() - 1 >= pos ? pos : list.size() - 1;
                    cacheProcess.setCacheLogIndex(list);
                    sendMsgSnack("上传成功", true);
                    loadRecord(currentPos);
                } else {
                    sendMsgSnack("上传失败", false);
                }
            }
        }).start();
    }

    private boolean requestUpload() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = new Random().nextInt(2);
        return i == 0 ? true : false;
    }


    @Override
    public void deleteRecord(final int pos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<LogIndex> list = cacheProcess.getCacheLogIndex();
                list.remove(pos);
                int currentPos = list.size() - 1 >= pos ? pos : list.size() - 1;
                cacheProcess.setCacheLogIndex(list);
                sendMsgSnack("删除成功", true);
                loadRecord(currentPos);
            }
        }).start();
    }

    @Override
    public void editRecordTitle(final int pos, final String titleName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<LogIndex> list = cacheProcess.getCacheLogIndex();
                LogIndex logIndex = list.get(pos);
                logIndex.aliasFileName = titleName;
                list.remove(pos);
                list.add(pos, logIndex);
                cacheProcess.setCacheLogIndex(list);
                loadRecord(pos);
            }
        }).start();
    }

    @Override
    public void loadRecord(final int currentPos) {
        List list = cacheProcess.getCacheLogIndex();
        if (currentPos < 0) {
            sendMsgSnack("发生未知的错误", false);
            return;
        }
        if (currentPos >= list.size()) {
            sendMsgSnack("发生未知的错误", false);
            return;
        }
        final LogIndex logIndex = (LogIndex) list.get(currentPos);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendMsgLoadLog(currentPos, logIndex);
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
