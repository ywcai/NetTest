package ywcai.ls.mobileutil.results.model;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.LocationService;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.http.UploadResult;
import ywcai.ls.mobileutil.login.model.MyUser;
import ywcai.ls.mobileutil.results.presenter.inf.LogIndexInf;
import ywcai.ls.mobileutil.setting.LogEntity;

public class LogIndex {
    public int cacheTypeIndex = 0;//取自AppConfig中的索引
    public String cacheFileName = "日志名称";//名字等于TYPE+"-"+START_TIME格式。后台使用，前台不展示
    public String aliasFileName = "标题名称";//自行编辑的别名，在列表中显示，默认显示当前时间。
    public String remarks = "结果摘要";//备注信息。默认为pingState中ip,max,min,avg,loss,total的格式化信息。
    public String logTime = "日志起始日期";
    public String addr = "未知地址";//最终保存数据时的位置。
    public long recordId=0;
    public LogIndexInf logIndexInf;

    public void setListener(LogIndexInf _logIndexInf) {
        logIndexInf = _logIndexInf;
    }

    public void setAddr() {
        final LocationService locationService = new LocationService(MainApplication.getInstance().getApplicationContext());
        locationService.registerListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null) {
                    if (bdLocation.getCity() != null) {
                        if (!bdLocation.getCity().equals("null")) {
                            addr = bdLocation.getCity() + "" + bdLocation.getDistrict() + "" + bdLocation.getStreet() + "" + bdLocation.getStreetNumber();
                        }
                    }
                }
                CacheProcess.getInstance().addCacheLogIndex(LogIndex.this);
                sendMsgPopSnackTip("本地数据保存成功", true);
                locationService.stop();
                locationService.unregisterListener();
            }
        });
        locationService.start();
    }

    public void setAddrAndUpload() {
        final LocationService locationService = new LocationService(MainApplication.getInstance().getApplicationContext());
        locationService.registerListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null) {
                    if (bdLocation.getCity() != null) {
                        if (!bdLocation.getCity().equals("null")) {
                            addr = bdLocation.getCity() + "" + bdLocation.getDistrict() + "" + bdLocation.getStreet() + "" + bdLocation.getStreetNumber();
                        }
                    }
                }
                locationService.stop();
                locationService.unregisterListener();
                MyUser myUser = CacheProcess.getInstance().getCacheUser();
                if (myUser == null) {
                    sendMsgPopSnackTip("上传到云端必须先登录账户", false);
                    return;
                }
                LogEntity logEntity = new LogEntity();
                logEntity.logIndex = LogIndex.this;
                logEntity.data = CacheProcess.getInstance().getCache(cacheFileName);
                upLoad(myUser.userid, logEntity);
            }
        });
        locationService.start();
    }

    private void upLoad(long userid, LogEntity logEntity) {
        HttpService httpService = RetrofitFactory.getHttpService();
        httpService.addRecord(userid, logEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<UploadResult>() {
                    @Override
                    protected void success(UploadResult uploadResult) {
                        logIndexInf.complete();
                        sendMsgPopSnackTip("成功上传到云端", true);
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        sendMsgPopSnackTip("上传失败:" + msg, false);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        sendMsgPopSnackTip("上传失败:" + e.toString(), false);
                    }
                });

    }

    private void sendMsgPopSnackTip(String tip, boolean success) {
        MsgHelper.sendEvent(GlobalEventT.global_pop_snack_tip, tip, success);
    }
}
