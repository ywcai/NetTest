package ywcai.ls.mobileutil.tools.Wifi.presenter;


import com.github.mikephil.charting.charts.LineChart;


import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.util.statics.MyTime;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.UpdateFragmentFourInf;

public class UpdateFragmentFour implements UpdateFragmentFourInf {
    private WifiState wifiState;


    public UpdateFragmentFour(WifiState wifiState) {
        this.wifiState = wifiState;
    }


    @Override
    public void refreshChannelLineRecord(int[] channelSum) {
        sendMsgRefreshLineChart(channelSum);
    }

    @Override
    public void switchLineChartSelect() {
        sendMsgSwitchLineChart();
    }

    @Override
    public void saveBitMap(final LineChart wifiChannelRecord) {
        String name = "wifi信道占用情况" + MyTime.getDetailTime();
        boolean isSuccess = wifiChannelRecord.saveToGallery(name, 100);
        String tip = isSuccess ? "保存位图到本地成功" : "保存位图到本地失败";
        sendMsgMainPopBottomTip(tip, isSuccess);
    }

    private void sendMsgRefreshLineChart(int[] allChannelSum) {

        MsgHelper.sendStickEvent(GlobalEventT.wifi_refresh_three_line, "", allChannelSum);

    }

    private void sendMsgSwitchLineChart() {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_switch_2d4g, "", wifiState.choose2d4G);
    }

    private void sendMsgMainPopBottomTip(String tip, boolean success) {
        MsgHelper.sendEvent(GlobalEventT.global_pop_snack_tip, tip, success);
    }


}
