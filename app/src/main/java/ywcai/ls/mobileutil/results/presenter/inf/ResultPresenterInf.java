package ywcai.ls.mobileutil.results.presenter.inf;

import java.util.List;

/**
 * Created by zmy_11 on 2017/10/13.
 */

public interface ResultPresenterInf {

    void pressLocalBtn();

    void pressRemoteBtn();

    void onClickCard(int pos);

    void selectDataTypeAll();

    void selectDataType(int index, boolean b);

    void initTagStatus();

    void filterDataForTag();

    void pullDown();
}
