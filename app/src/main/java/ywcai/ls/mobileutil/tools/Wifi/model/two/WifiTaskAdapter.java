package ywcai.ls.mobileutil.tools.Wifi.model.two;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;


public class WifiTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<WifiEntry> list;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private View.OnClickListener clickListener;

    public WifiTaskAdapter(Context _context, List<WifiEntry> _list) {
        context = _context;
        list = _list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == -1) {
//            View view = LayoutInflater.from(
//                    context).inflate(R.layout.list_wifi_task_empty_tip, parent,
//                    false);
//            WifiTaskEmptyHolder emptyHolder = new WifiTaskEmptyHolder(view);
//            return emptyHolder;
//        } else {
        View view = LayoutInflater.from(
                context).inflate(R.layout.list_wifi_task, parent,
                false);
        WifiTaskViewHolder holder = new WifiTaskViewHolder(view);
        view.setClickable(true);
        return holder;
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder wifiTaskViewTemp, int position) {
        wifiTaskViewTemp.itemView.setTag(position);
        wifiTaskViewTemp.itemView.setClickable(true);
        if (list.size() > 0) {
            wifiTaskViewTemp.itemView.setOnClickListener(this);
            ((WifiTaskViewHolder) wifiTaskViewTemp).btShowChartLine.setTag(position);
            ((WifiTaskViewHolder) wifiTaskViewTemp).btShowChartLine.setClickable(true);
            ((WifiTaskViewHolder) wifiTaskViewTemp).btShowChartLine.setOnClickListener(clickListener);
            WifiEntry wifiEntry = list.get(position);
            int per = 0;
            int max = -40;
            int min = -140;
            String dbm = "";
            if (wifiEntry.dbm > -160) {
                per = (wifiEntry.dbm - min) >= 0 ? (wifiEntry.dbm - min) : 0;
                dbm = wifiEntry.dbm + "dbm";
            } else {
                per = 0;
                dbm = "无信号";
            }
            ((WifiTaskViewHolder) wifiTaskViewTemp).wifiDbm.setProgress(per);
            ((WifiTaskViewHolder) wifiTaskViewTemp).wifiDbmT.setText(dbm);
            ((WifiTaskViewHolder) wifiTaskViewTemp).wifiMac.setText(wifiEntry.bssid + "");
            ((WifiTaskViewHolder) wifiTaskViewTemp).wifiSsid.setText(wifiEntry.ssid + "");
            ((WifiTaskViewHolder) wifiTaskViewTemp).wifiSsid.setTextColor(ContextCompat.getColor(context
                    , AppConfig.colors[position]));
            ((WifiTaskViewHolder) wifiTaskViewTemp).wifiCh.setText("信道" + wifiEntry.channel + "");
            ((WifiTaskViewHolder) wifiTaskViewTemp).wifiF.setText(wifiEntry.frequency + "MHZ");
            if (wifiEntry.isConnWifi && !dbm.equals("无信号")) {
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiConn.setTextColor(Color.YELLOW);
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiConn.setVisibility(View.VISIBLE);
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiSpeed.setTextColor(Color.YELLOW);
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiSpeed.setVisibility(View.VISIBLE);
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiIp.setTextColor(Color.YELLOW);
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiIp.setVisibility(View.VISIBLE);

                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiSpeed.setText(wifiEntry.speed + "Mbps");
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiIp.setText(wifiEntry.device);

            } else {
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiConn.setVisibility(View.GONE);
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiSpeed.setVisibility(View.GONE);
                ((WifiTaskViewHolder) wifiTaskViewTemp).wifiIp.setVisibility(View.GONE);
            }
            if (wifiEntry.isShowInChart) {
                ((CardView) ((WifiTaskViewHolder) wifiTaskViewTemp).itemView).setCardBackgroundColor(ContextCompat.getColor(context
                        , AppConfig.colors[position]));
                ((WifiTaskViewHolder) wifiTaskViewTemp).btShowChartLine.setText("隐藏折线");
                ((WifiTaskViewHolder) wifiTaskViewTemp).btShowChartLine.setGhost(false);
            } else {
                ((CardView) ((WifiTaskViewHolder) wifiTaskViewTemp).itemView).setCardBackgroundColor(ContextCompat.getColor(context
                        , R.color.card_shadow));
                ((WifiTaskViewHolder) wifiTaskViewTemp).btShowChartLine.setText("显示折线");
                ((WifiTaskViewHolder) wifiTaskViewTemp).btShowChartLine.setGhost(true);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (list.size() == 0) {
//            return -1;
//        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
//        return list.size() > 0 ? list.size() : 1;
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.OnClickItem(v, (int) v.getTag());
        }
    }

    public void setOnclickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setBtnHideListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }


}
