package ywcai.ls.mobileutil.menu.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import ywcai.ls.mobileutil.R;

public class TitleTipAdapter extends RecyclerView.Adapter<TitleTipViewHolder> {
    private List<TitleTipMenu> list;
    private Context context;

    public TitleTipAdapter(Context _context, List<TitleTipMenu> _list)
    {
        context=_context;
        list=_list;
    }
    @Override
    public TitleTipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.list_menu_title_tip_grid, parent,
                false);
        TitleTipViewHolder holder = new TitleTipViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TitleTipViewHolder titleTipViewHolder, int position) {
        titleTipViewHolder.itemView.setTag(position);
        TitleTipMenu titleTipMenu = list.get(position);
        titleTipViewHolder.counts.setText(titleTipMenu.titleContent);
        titleTipViewHolder.titleTip.setText(titleTipMenu.titleTopText);
        titleTipViewHolder.counts.setTextColor(titleTipMenu.textColor);
        titleTipViewHolder.titleTip.setTextColor(titleTipMenu.textColor);
        titleTipViewHolder.titleImg.setImageDrawable(titleTipMenu.titleIcon);
        titleTipViewHolder.relativeLayout.setBackground(titleTipMenu.titleBorder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
