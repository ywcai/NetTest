package ywcai.ls.mobileutil.article.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;
import ywcai.ls.mobileutil.results.model.EmptyHolder;

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<UserComment> list;
    private Context context;
    private OnItemClickListener onItemClickListener, onSubItemClickListener;

    public CommentListAdapter(Context _context, List<UserComment> _list) {
        context = _context;
        list = _list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == -1) {
            View view = LayoutInflater.from(
                    context).inflate(R.layout.list_empty_tip, parent,
                    false);
            EmptyHolder emptyHolder = new EmptyHolder(view);
            return emptyHolder;
        }

        View view = LayoutInflater.from(
                context).inflate(R.layout.list_article_comment_card, parent,
                false);
        ArticleCommentListViewHolder holder = new ArticleCommentListViewHolder(view);
        view.setClickable(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder articleViewTemp, final int position) {
        articleViewTemp.itemView.setTag(position);
        if (list.size() > 0) {
            articleViewTemp.itemView.setOnClickListener(this);
            UserComment userComment = list.get(position);
            ((ArticleCommentListViewHolder) articleViewTemp).comment_user_nickname.setText(userComment.nickName + "");
            ((ArticleCommentListViewHolder) articleViewTemp).comment_create_time.setText(userComment.createTime + "");
            ((ArticleCommentListViewHolder) articleViewTemp).comment_content.setText(userComment.detail + "");
            ((ArticleCommentListViewHolder) articleViewTemp).comment_create_addr.setText(userComment.createAddr + "");
            ((ArticleCommentListViewHolder) articleViewTemp).comment_praise_number.setText(userComment.praise + "");
            ((ArticleCommentListViewHolder) articleViewTemp).comment_praise_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSubItemClickListener != null) {
                        onSubItemClickListener.OnClickItem(v, position);
                    }
                }
            });
            if (userComment.userImg == null) {
                Glide.with(context).load(R.drawable.user_login).
                        into(((ArticleCommentListViewHolder) articleViewTemp).comment_user_head);
                return;
            }
            if (userComment.userImg.equals("")) {

            } else {
                Glide.with(context).load(userComment.userImg).
                        into(((ArticleCommentListViewHolder) articleViewTemp).comment_user_head);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return -1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 1;
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

    public void setOnclickComment(OnItemClickListener listener) {
        this.onSubItemClickListener = listener;
    }
}
