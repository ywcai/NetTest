package ywcai.ls.mobileutil.results.presenter.inf;

import java.util.List;

/**
 * Created by zmy_11 on 2017/10/13.
 */

public interface ResultPresenterInf {
    void refreshData();
    void pressLocalBtn();
    void pressRemoteBtn();
    void deleteData(int[] logIndex);
    void onClickCard(int pos);


    void selectDataTypeAll();
    void selectDataType(int index);
    void initTagStatus();
}
