package ywcai.ls.mobileutil.login.Presenter;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.login.Presenter.inf.LoginInf;
import ywcai.ls.mobileutil.login.model.User;

public class Login implements LoginInf {
    private Tencent tencent;

    @Override
    public void loginForSelf(String userId, String psw) {

    }

    @Override
    public void loginForQQ(Activity activity, IUiListener qqListener) {
        if (tencent == null) {
            tencent = Tencent.createInstance(AppConfig.QQ_APP_ID, activity);
        }
        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", qqListener);
        } else {
            //已经登录
            MsgHelper.sendEvent(GlobalEventT.test_tip, "已登录", null);
        }
    }

    @Override
    public void loginForMM(Activity activity) {

    }

    @Override
    public void reg(String userId, String psw) {

    }

    @Override
    public void findPassword(String userId) {

    }

    @Override
    public User getUserEntry(String openid) {


        return null;
    }


}
