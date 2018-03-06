package ywcai.ls.mobileutil.article.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;

public class ArticleListViewHolder extends RecyclerView.ViewHolder {

    public TextView article_comment, article_top;
    public TextView article_title, article_remarks, article_auth_nickname, article_createTime;
    public ImageView article_thumb;
    public ImageView article_auth_img, article_comment_btn;

    public ArticleListViewHolder(View itemView) {
        super(itemView);
        article_comment_btn = (ImageView) itemView.findViewById(R.id.article_card_btn_comment);
        article_comment = (TextView) itemView.findViewById(R.id.article_card_bottom_comment);
        article_top = (TextView) itemView.findViewById(R.id.article_card_bottom_top);
        article_title = (TextView) itemView.findViewById(R.id.article_card_main_title);
        article_remarks = (TextView) itemView.findViewById(R.id.article_card_main_remarks);
        article_auth_nickname = (TextView) itemView.findViewById(R.id.article_card_top_auth_nickname);
        article_createTime = (TextView) itemView.findViewById(R.id.article_card_top_create_date);
        article_thumb = (ImageView) itemView.findViewById(R.id.article_card_main_thumb);
        article_auth_img = (ImageView) itemView.findViewById(R.id.article_card_top_auth_img);
    }
}

