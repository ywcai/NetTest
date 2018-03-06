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

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<ArticleIndex> list;
    private Context context;
    private OnItemClickListener onItemClickListener, onSubItemClickListener;

    public ArticleAdapter(Context _context, List<ArticleIndex> _list) {
        context = _context;
        list = _list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.list_article_main_card, parent,
                false);
        ArticleListViewHolder holder = new ArticleListViewHolder(view);
        view.setClickable(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder articleViewTemp, final int position) {
        articleViewTemp.itemView.setTag(position);
        if (list.size() > 0) {
            articleViewTemp.itemView.setOnClickListener(this);
            ArticleIndex articleIndex = list.get(position);
            ((ArticleListViewHolder) articleViewTemp).article_comment.setText(articleIndex.comment + "");
            ((ArticleListViewHolder) articleViewTemp).article_top.setText(articleIndex.top + "");
            ((ArticleListViewHolder) articleViewTemp).article_title.setText(articleIndex.title + "");
            ((ArticleListViewHolder) articleViewTemp).article_remarks.setText(articleIndex.remarks + "");
            ((ArticleListViewHolder) articleViewTemp).article_auth_nickname.setText(articleIndex.authNickname + "");
            ((ArticleListViewHolder) articleViewTemp).article_createTime.setText(articleIndex.createTime + "");
            ((ArticleListViewHolder) articleViewTemp).article_comment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSubItemClickListener != null) {
                        onSubItemClickListener.OnClickItem(v, position);
                    }
                }
            });
            if (articleIndex.authImg == null) {
                Glide.with(context).load(R.drawable.user_out).
                        into(((ArticleListViewHolder) articleViewTemp).article_auth_img);
            } else {
                if (articleIndex.authImg.equals("")) {
                    Glide.with(context).load(R.drawable.user_out).
                            into(((ArticleListViewHolder) articleViewTemp).article_auth_img);
                } else {
                    Glide.with(context).load(articleIndex.authImg).
                            into(((ArticleListViewHolder) articleViewTemp).article_auth_img);
                }
            }

            if (articleIndex.thumbUrl == null) {
                ((ArticleListViewHolder) articleViewTemp).article_thumb.setVisibility(View.GONE);
                return;
            }
            if (articleIndex.thumbUrl.equals("")) {
                ((ArticleListViewHolder) articleViewTemp).article_thumb.setVisibility(View.GONE);
                return;
            }
            ((ArticleListViewHolder) articleViewTemp).article_thumb.setVisibility(View.VISIBLE);
            Glide.with(context).load(articleIndex.thumbUrl).
                    placeholder(R.drawable.main_menu_top).
                    into(((ArticleListViewHolder) articleViewTemp).article_thumb);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
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

    public void setOnclickComment(OnItemClickListener listener) {
        this.onSubItemClickListener = listener;
    }
}
