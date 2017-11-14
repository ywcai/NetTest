package ywcai.ls.mobileutil.identity.Presenter.inf;

/**
 * Created by zmy_11 on 2017/7/16.
 */

public interface LoginInf {
    void loginForSelf(String userId,String psw);
    void loginForQQ();
    void loginForMM();
    void reg(String userId,String psw);
    void findPassword(String userId);
}
