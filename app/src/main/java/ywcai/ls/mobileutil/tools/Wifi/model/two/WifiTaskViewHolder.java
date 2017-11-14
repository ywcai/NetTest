package ywcai.ls.mobileutil.tools.Wifi.model.two;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;
import ywcai.ls.mobileutil.R;

public class WifiTaskViewHolder extends RecyclerView.ViewHolder {

    public TextView wifiSsid, wifiMac, wifiSpeed, wifiConn, wifiF, wifiCh,wifiIp,wifiDbmT;
    public ContentLoadingProgressBar wifiDbm;
    public FancyButton btShowChartLine;

    public WifiTaskViewHolder(View itemView) {
        super(itemView);
        btShowChartLine=(FancyButton)itemView.findViewById(R.id.btn_show_in_chart);
        wifiDbm = (ContentLoadingProgressBar) itemView.findViewById(R.id.wifi_dbm_progress);
        wifiDbmT= (TextView) itemView.findViewById(R.id.wifi_dbm_text);
        wifiSsid = (TextView) itemView.findViewById(R.id.wifi_ssid);
        wifiMac = (TextView) itemView.findViewById(R.id.wifi_mac);
        wifiSpeed = (TextView) itemView.findViewById(R.id.wifi_speed);
        wifiConn = (TextView) itemView.findViewById(R.id.wifi_conn);
        wifiF = (TextView) itemView.findViewById(R.id.wifi_f);
        wifiCh = (TextView) itemView.findViewById(R.id.wifi_channel);
        wifiIp = (TextView) itemView.findViewById(R.id.wifi_ip);
    }
}

