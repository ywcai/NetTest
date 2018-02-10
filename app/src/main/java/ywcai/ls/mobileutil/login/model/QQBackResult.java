package ywcai.ls.mobileutil.login.model;

/**
 * Created by zmy_11 on 2018/2/2.
 */

public class QQBackResult {
    public int ret = -1;
    public String pay_token = "pay_token",
            pf = "pf",
            expires_in = "7776000",
            openid = "openid",
            pfkey = "pfkey",
            msg = "fail",
            access_token = "access_token";

    @Override
    public String toString() {
        return "QQBackResult{" +
                "ret=" + ret +
                ", pay_token='" + pay_token + '\'' +
                ", pf='" + pf + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", openid='" + openid + '\'' +
                ", pfkey='" + pfkey + '\'' +
                ", msg='" + msg + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}
