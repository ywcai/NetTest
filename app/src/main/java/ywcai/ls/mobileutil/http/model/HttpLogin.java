package ywcai.ls.mobileutil.http.model;

import ywcai.ls.mobileutil.global.model.User;
import ywcai.ls.mobileutil.login.model.LoginResult;

/**
 * Created by zmy_11 on 2017/7/15.
 */

public class HttpLogin {

    public LoginResult requestAuth(User user)
    {
        LoginResult loginResult=new LoginResult();
        loginResult.isLogin=true;
        loginResult.accessToken="testToken";
        return loginResult;
    }
}
