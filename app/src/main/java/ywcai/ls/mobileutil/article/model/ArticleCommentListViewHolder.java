package ywcai.ls.mobileutil.article.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;

public class ArticleCommentListViewHolder extends RecyclerView.ViewHolder {

    public TextView comment_content,
            comment_create_time,
            comment_create_addr,
            comment_user_nickname,
            comment_praise_number;

    public ImageView comment_user_head, comment_praise_btn;

    public ArticleCommentListViewHolder(View itemView) {
        super(itemView);
        comment_praise_number=(TextView) itemView.findViewById(R.id.article_comment_praise_number);
        comment_content = (TextView) itemView.findViewById(R.id.article_comment_main);
        comment_create_time = (TextView) itemView.findViewById(R.id.article_comment_create_time);
        comment_create_addr = (TextView) itemView.findViewById(R.id.article_comment_create_addr);
        comment_user_nickname = (TextView) itemView.findViewById(R.id.article_comment_user_nickname);
        comment_praise_btn = (ImageView) itemView.findViewById(R.id.article_comment_praise_btn);
        comment_user_head = (ImageView) itemView.findViewById(R.id.article_comment_user_img);
    }
}

