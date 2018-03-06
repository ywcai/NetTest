package ywcai.ls.mobileutil.article.model;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import ywcai.ls.mobileutil.article.model.inf.CallBackUpdate;
import ywcai.ls.mobileutil.global.model.LocationService;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.MD5;
import ywcai.ls.mobileutil.global.util.statics.MyTime;
import ywcai.ls.mobileutil.login.model.MyUser;

/**
 * Created by zmy_11 on 2018/3/3.
 */

public class UserComment {
    public long articleId = 0;
    public long id = 0;
    public long userId = 0;//提交用户ID，非文章ID
    public String createTime = "";
    public String detail = "";
    public String createAddr = "";
    public int commentType = 0;//是文章的评论还是评论的评论,0默认是文章的评论
    String sign = "";//数字签名等于,所有属性按照首字母顺序连接后签名。//openid和临时接入秘钥放再最后连接
    public int praise = 0;
    public String userImg = "";
    public String nickName = "";

    public void setData(long articleId, String commentContent, CallBackUpdate callBackUpdate) {
        this.articleId = articleId;
        this.detail = commentContent;
        userId = CacheProcess.getInstance().getCacheUser().userid;
        createTime = MyTime.getDetailTime();
        setAddr(callBackUpdate);

    }

    private void setAddr(final CallBackUpdate callBackUpdate) {
        final LocationService locationService = new LocationService(MainApplication.getInstance().getApplicationContext());
        locationService.registerListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null) {
                    if (bdLocation.getCity() != null) {
                        if (!bdLocation.getCity().equals("null")) {
                            createAddr = bdLocation.getCity() + "" + bdLocation.getDistrict() + "" + bdLocation.getStreet() + "" + bdLocation.getStreetNumber();
                        }
                    }
                }
                locationService.stop();
                locationService.unregisterListener();
                MyUser user = CacheProcess.getInstance().getCacheUser();
                String openId = user.openid;
//                String accessToken = "JIMI";
                nickName=user.nickname;
                userImg=user.userImg;
                sign = MD5.md5(articleId + userId + createTime + detail + openId  + "YWCAI");
                callBackUpdate.updateComment();
            }
        });
        locationService.start();
    }
}
