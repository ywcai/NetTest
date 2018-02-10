package ywcai.ls.mobileutil.results.presenter.inf;

/**
 * Created by zmy_11 on 2018/1/6.
 */

public interface DetailActionInf {

    void downloadRecordForRemote(int pos);

    void shareRecordForRemote(int pos);

    void uploadRecord(int pos);

    void deleteRecord(int pos);

    void editRecordTitle(int pos, String titleName);

    void loadRecord(int currentPos);

    void prev(int currentPos);

    void next(int currentPos);
}
