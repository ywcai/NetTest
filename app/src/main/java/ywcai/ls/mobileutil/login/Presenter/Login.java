package ywcai.ls.mobileutil.login.Presenter;

import ywcai.ls.mobileutil.global.presenter.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.MD5;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.model.User;
import ywcai.ls.mobileutil.login.Presenter.inf.LoginInf;
import ywcai.ls.mobileutil.login.cfg.LoginInfoT;
import ywcai.ls.mobileutil.login.model.HttpRequest;
import ywcai.ls.mobileutil.login.model.LoginEventT;
import ywcai.ls.mobileutil.login.model.LoginResult;

public class Login implements LoginInf{
    User tempUser=new User();
    private CacheProcess cache=new CacheProcess();
    @Override
    public void loginForSelf(String userId, String psw) {
        ValidateFormat validate = new ValidateFormat();
        if ((!validate.checkUserName(userId)) || (!validate.checkPsw(psw))) {
            MsgHelper.sendEvent(LoginEventT.local_input_err,"",null);
            return;
        }
        String md5Psw = MD5.md5(psw);
        tempUser.userId=userId;
        tempUser.md5psw=md5Psw;
        tempUser.loginChannel = LoginInfoT.login_channel_self;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequest httpRequest = new HttpRequest();
                LoginResult result = httpRequest.requestAuth(tempUser);
                backResult(result);
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
        if(result==null)
        {
            MsgHelper.sendEvent(LoginEventT.net_err,"",null);
            return;
        }
        if(result.isLogin)
        {
            cache.setCacheUser(tempUser);
            MsgHelper.sendEvent(LoginEventT.server_pass,"",tempUser);
        }
        else
        {
            MsgHelper.sendEvent(LoginEventT.server_refuse,result.errCode,null);
        }
    }




}
