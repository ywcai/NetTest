package ywcai.ls.mobileutil.menu.model;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;


public class MenuAdapter extends RecyclerView.Adapter<MenuListViewHolder> implements View.OnClickListener {
    private List<IndexMenu> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public MenuAdapter(Context _context,List<IndexMenu> _list)
    {
        context=_context;
        list=_list;
    }
    @Override
    public MenuListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.listview_main_menu, parent,
                false);
        MenuListViewHolder holder = new MenuListViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MenuListViewHolder deviceListViewTemp, int position) {
        deviceListViewTemp.itemView.setTag(position);
        IndexMenu indexMenu = list.get(position);
        deviceListViewTemp.menu_text.setText(indexMenu.menuText);
        deviceListViewTemp.menu_img.setImageDrawable(indexMenu.imgUrl);
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
