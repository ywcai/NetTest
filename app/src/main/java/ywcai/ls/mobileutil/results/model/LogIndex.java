package ywcai.ls.mobileutil.results.model;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.LocationService;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;

public class LogIndex {
    public int cacheTypeIndex = 0;//取自AppConfig中的索引
    public String cacheFileName = "日志名称";//名字等于TYPE+"-"+START_TIME格式。后台使用，前台不展示
    public String aliasFileName = "标题名称";//自行编辑的别名，在列表中显示，默认显示当前时间。
    public String remarks = "结果摘要";//备注信息。默认为pingState中ip,max,min,avg,loss,total的格式化信息。
    public String logTime = "日志起始日期";
    public String addr = "未知地址";//最终保存数据时的位置。

    public void setAddr() {
//        if (isNeedValid()) {
//            //如果版本大于23
//            if (!isPermission()) {
//                //并且没有赋予权限，则直接返回
//                addr = "未知地址";
//                CacheProcess.getInstance().addCacheLogIndex(LogIndex.this);
//                sendMsgPopSnackTip("本地数据保存成功", true);
//                return;
//            }
//        }
        LocationService locationService = MainApplication.getInstance().getLocationService();
        locationService.registerListener(mListener);
        locationService.start();
    }

    private boolean isNeedValid() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermission() {
        boolean isPermission = MainApplication.getInstance().getApplicationContext().
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        isPermission = isPermission && MainApplication.getInstance().getApplicationContext().
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        isPermission = isPermission && MainApplication.getInstance().getApplicationContext().
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        isPermission = isPermission && MainApplication.getInstance().getApplicationContext().
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        isPermission = isPermission && MainApplication.getInstance().getApplicationContext().
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return isPermission;
    }

    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
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
            LocationService locationService = MainApplication.getInstance().getLocationService();
            locationService.stop();
            locationService.unregisterListener(this);
        }
    };

    private void sendMsgPopSnackTip(String tip, boolean success) {
        MsgHelper.sendEvent(GlobalEventT.global_pop_snack_tip, tip, success);
    }

}
