package ywcai.ls.mobileutil.setting;

import ywcai.ls.mobileutil.login.model.MyUser;

/**
 * Created by zmy_11 on 2018/2/6.
 */

public interface SetActionInf {
    void clearRecord();
    void updateRecord(MyUser myUser);
    void editNickName(MyUser myUser,String nickname);
    void popHelpMsg();
    void popContract();

    void loginOut();
}
