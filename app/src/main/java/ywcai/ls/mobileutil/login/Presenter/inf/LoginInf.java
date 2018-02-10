package ywcai.ls.mobileutil.login.Presenter.inf;

import android.app.Activity;

import com.tencent.tauth.IUiListener;

import ywcai.ls.mobileutil.login.model.User;

/**
 * Created by zmy_11 on 2017/7/16.
 */

public interface LoginInf {
    void loginForSelf(String userId,String psw);
    void loginForQQ(Activity activity, IUiListener qqListener);
    void loginForMM(Activity activity);
    void reg(String userId,String psw);
    void findPassword(String userId);

    User getUserEntry(String openid);
}
