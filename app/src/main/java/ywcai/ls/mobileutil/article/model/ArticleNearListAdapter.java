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

public class ArticleNearListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<ArticleIndex> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ArticleNearListAdapter(Context _context, List<ArticleIndex> _list) {
        context = _context;
        list = _list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.list_article_near_card, parent,
                false);
        ArticleNearListViewHolder holder = new ArticleNearListViewHolder(view);
        view.setClickable(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder articleViewTemp, final int position) {
        articleViewTemp.itemView.setTag(position);
        if (list.size() > 0) {
            articleViewTemp.itemView.setOnClickListener(this);
            ArticleIndex articleIndex = list.get(position);
            ((ArticleNearListViewHolder) articleViewTemp).article_comment.setText(articleIndex.comment + "");
            ((ArticleNearListViewHolder) articleViewTemp).article_top.setText(articleIndex.top + "");
            ((ArticleNearListViewHolder) articleViewTemp).article_title.setText(articleIndex.title + "");
            ((ArticleNearListViewHolder) articleViewTemp).article_auth_nickname.setText(articleIndex.authNickname + "");
            ((ArticleNearListViewHolder) articleViewTemp).article_createTime.setText(articleIndex.createTime + "");
            if (articleIndex.authImg == null) {
                Glide.with(context).load(R.drawable.user_out).
                        into(((ArticleNearListViewHolder) articleViewTemp).article_auth_img);
            } else {
                if (articleIndex.authImg.equals("")) {
                    Glide.with(context).load(R.drawable.user_out).
                            into(((ArticleNearListViewHolder) articleViewTemp).article_auth_img);
                } else {
                    Glide.with(context).load(articleIndex.authImg).
                            into(((ArticleNearListViewHolder) articleViewTemp).article_auth_img);
                }
            }
            if (articleIndex.thumbUrl == null) {
                Glide.with(context).load(R.drawable.main_menu_top).
                        into(((ArticleNearListViewHolder) articleViewTemp).article_thumb);
            } else {
                if (articleIndex.thumbUrl.equals("")) {
                    Glide.with(context).load(R.drawable.main_menu_top).
                            into(((ArticleNearListViewHolder) articleViewTemp).article_thumb);
                } else {
                    Glide.with(context).load(articleIndex.thumbUrl).
                            placeholder(R.drawable.main_menu_top).
                            into(((ArticleNearListViewHolder) articleViewTemp).article_thumb);
                }
            }
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
}
