package ywcai.ls.mobileutil.identity.Presenter;

import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MD5;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.identity.model.User;
import ywcai.ls.mobileutil.identity.Presenter.inf.LoginInf;
import ywcai.ls.mobileutil.identity.cfg.LoginInfoT;
import ywcai.ls.mobileutil.identity.model.LoginEventT;
import ywcai.ls.mobileutil.identity.model.LoginResult;

public class Login implements LoginInf {
    User tempUser = new User();
    private CacheProcess cache = CacheProcess.getInstance();

    @Override
    public void loginForSelf(String userId, String psw) {
        ValidateFormat validate = new ValidateFormat();
        if ((!validate.checkUserName(userId)) || (!validate.checkPsw(psw))) {
            MsgHelper.sendEvent(LoginEventT.local_input_err, "", null);
            return;
        }
        String md5Psw = MD5.md5(psw);
        tempUser.userId = userId;
        tempUser.md5psw = md5Psw;
        tempUser.loginChannel = LoginInfoT.login_channel_self;
        new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();
    }

    @Override
    public void loginForQQ() {

    }

    @Override
    public void loginForMM() {

    }

    @Override
    public void reg(String userId, String psw) {

    }

    @Override
    public void findPassword(String userId) {

    }


    private void backResult(LoginResult result) {
        if (result == null) {
            MsgHelper.sendEvent(LoginEventT.net_err, "", null);
            return;
        }
        if (result.isLogin) {
            cache.setCacheUser(tempUser);
            MsgHelper.sendEvent(LoginEventT.server_pass, "", tempUser);
        } else {
            MsgHelper.sendEvent(LoginEventT.server_refuse, result.errCode, null);
        }
    }


}
