package ywcai.ls.mobileutil.results.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;


public class ResultAdapter extends RecyclerView.Adapter< RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<LogIndex> list;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int[] menuImg = AppConfig.getMenuIconRes();

    public ResultAdapter(Context _context, List<LogIndex> _list) {
        context = _context;
        list = _list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==-1)
        {
            View view = LayoutInflater.from(
                    context).inflate(R.layout.list_empty_tip, parent,
                    false);
            EmptyHolder emptyHolder=new EmptyHolder(view);
            return emptyHolder;
        }
        else {
            View view = LayoutInflater.from(
                    context).inflate(R.layout.list_result_main_card, parent,
                    false);
            ResultListViewHolder holder = new ResultListViewHolder(view);
            view.setOnClickListener(this);
            view.setClickable(true);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder resultListViewTemp, int position) {
        resultListViewTemp.itemView.setTag(position);
        if (list.size() > 0) {
            LogIndex logIndex = list.get(position);
            ((ResultListViewHolder)resultListViewTemp).result_alias.setText(logIndex.aliasFileName);
            ((ResultListViewHolder)resultListViewTemp).result_remarks.setText(logIndex.remarks);
            ((ResultListViewHolder)resultListViewTemp).result_time.setText(logIndex.logTime);
            ((ResultListViewHolder)resultListViewTemp).result_type.setImageDrawable(ContextCompat.getDrawable(context, menuImg[logIndex.cacheTypeIndex]));
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
        return list.size()>0?list.size():1;
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
