package ywcai.ls.mobileutil.results.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;

public class ResultListViewHolder extends RecyclerView.ViewHolder {

    public TextView result_alias, result_time, result_remarks;
    public ImageView result_type;

    public ResultListViewHolder(View itemView) {
        super(itemView);
        result_alias = (TextView) itemView.findViewById(R.id.wifi_ssid);
        result_time = (TextView) itemView.findViewById(R.id.result_time);
        result_remarks = (TextView) itemView.findViewById(R.id.result_remarks);
        result_type = (ImageView) itemView.findViewById(R.id.result_type);
    }
}

