package ywcai.ls.mobileutil.menu.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;

public class MenuListViewHolder extends RecyclerView.ViewHolder {
    public TextView menu_text;
    public ImageView menu_img;

    public MenuListViewHolder(View itemView) {
        super(itemView);
        menu_text = (TextView) itemView.findViewById(R.id.menu_text);
        menu_img=(ImageView) itemView.findViewById(R.id.menu_image);
    }
}

