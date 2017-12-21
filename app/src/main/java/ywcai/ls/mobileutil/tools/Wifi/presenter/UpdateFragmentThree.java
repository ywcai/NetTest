package ywcai.ls.mobileutil.tools.Wifi.presenter;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
 
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.UpdateFragmentThreeInf;


public class UpdateFragmentThree implements UpdateFragmentThreeInf {
    WifiState wifiState;

    int[] frequency;

    public UpdateFragmentThree(WifiState wifiState) {
        this.wifiState = wifiState;
    }

    @Override
    public void refreshChannelBarChart(int[] channelSum) {
        int[] sum;
        if (wifiState.choose2d4G) {
            sum = new int[AppConfig.INTS_CHANNEL_2D4G.length];
            for (int i = 0; i < AppConfig.INTS_CHANNEL_2D4G.length; i++) {
                sum[i] = channelSum[AppConfig.INTS_CHANNEL_2D4G[i]];
            }
        } else {
            sum = new int[AppConfig.INTS_CHANNEL_5G.length];
            for (int i = 0; i < AppConfig.INTS_CHANNEL_5G.length; i++) {
                sum[i] = channelSum[AppConfig.INTS_CHANNEL_5G[i]];
            }
        }
        sendMsgRefreshBarChart(sum);
    }

    @Override
    public void refreshFrequencyLevel(int[] channelSum) {
        frequency = new int[65];
        for (int i = 0; i < 13; i++) {
            frequency[i] += channelSum[AppConfig.INTS_CHANNEL_2D4G[i]];
            frequency[i + 1] += channelSum[AppConfig.INTS_CHANNEL_2D4G[i]];
            frequency[i + 2] += channelSum[AppConfig.INTS_CHANNEL_2D4G[i]];
            frequency[i + 3] += channelSum[AppConfig.INTS_CHANNEL_2D4G[i]];
            frequency[i + 4] += channelSum[AppConfig.INTS_CHANNEL_2D4G[i]];
        }
        sendMsgRefreshFrequencyLevel(frequency);
    }


    private void sendMsgRefreshBarChart(int[] channelSum) {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_refresh_three_bar, wifiState.choose2d4G + "", channelSum);
    }

    private void sendMsgRefreshFrequencyLevel(int[] _frequency) {
        MsgHelper.sendStickEvent(GlobalEventT.wifi_refresh_frequency_level, "", _frequency);
    }


}
