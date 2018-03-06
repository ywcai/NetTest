package ywcai.ls.mobileutil.article.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;

public class ArticleNearListViewHolder extends RecyclerView.ViewHolder {

    public TextView article_comment, article_top;
    public TextView article_title, article_auth_nickname, article_createTime;
    public ImageView article_thumb;
    public ImageView article_auth_img;

    public ArticleNearListViewHolder(View itemView) {
        super(itemView);

        article_comment = (TextView) itemView.findViewById(R.id.article_detail_near_comment_number);
        article_top = (TextView) itemView.findViewById(R.id.article_detail_near_top_number);
        article_title = (TextView) itemView.findViewById(R.id.article_detail_near_title);
        article_auth_nickname = (TextView) itemView.findViewById(R.id.article_detail_near_auth);
        article_createTime = (TextView) itemView.findViewById(R.id.article_detail_near_create_time);
        article_thumb = (ImageView) itemView.findViewById(R.id.article_detail_near_thumb);
        article_auth_img = (ImageView) itemView.findViewById(R.id.article_detail_near_img);
    }
}

