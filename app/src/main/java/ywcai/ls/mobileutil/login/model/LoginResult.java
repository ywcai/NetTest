package ywcai.ls.mobileutil.login.model;

/**
 * Created by zmy_11 on 2017/7/15.
 */

public class LoginResult {
    public boolean isLogin;
    public String accessToken;
    public String errCode;
    public String userId;//手机号

    @Override
    public String toString() {
        return "LoginResult{" +
                "isLogin=" + isLogin +
                ", accessToken='" + accessToken + '\'' +
                ", errCode='" + errCode + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
