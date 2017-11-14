package ywcai.ls.mobileutil.menu.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;

public class TitleTipViewHolder extends RecyclerView.ViewHolder {
    public TextView counts;
    public ImageView titleImg;
    public TextView titleTip;
    public RelativeLayout relativeLayout;

    public TitleTipViewHolder(View itemView) {
        super(itemView);
        counts= (TextView) itemView.findViewById(R.id.title_tip_content);
        titleImg=(ImageView) itemView.findViewById(R.id.title_tip_img);
        titleTip=(TextView) itemView.findViewById(R.id.title_tip_top);
        relativeLayout=(RelativeLayout) itemView.findViewById(R.id.title_tip_border);
    }
}

