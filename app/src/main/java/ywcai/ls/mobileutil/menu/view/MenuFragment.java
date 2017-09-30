package ywcai.ls.mobileutil.menu.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.presenter.ActivityRouter;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.menu.model.IndexMenu;
import ywcai.ls.mobileutil.menu.model.MenuAdapter;
import ywcai.ls.mobileutil.menu.model.MenuItemDecoration;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;


public class MenuFragment extends Fragment {
    private List<IndexMenu> menuList = new ArrayList();
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private View view;
    private boolean clickEnable=true;
    private String[] menus=new String[]{"PING","WIFI","基站信号","蓝牙扫描","内网扫描","端口扫描","经纬度","传感器","指南针","网络测速"};
    private int[] menuImg=new int[]{
            R.drawable.homepage_menu_ping,
            R.drawable.homepage_menu_wifi,
            R.drawable.homepage_menu_station,
            R.drawable.homepage_menu_ble,
            R.drawable.homepage_menu_lan,
            R.drawable.homepage_menu_more,
            R.drawable.homepage_menu_gps,
            R.drawable.homepage_menu_sensor,
            R.drawable.homepage_menu_orientation,
            R.drawable.homepage_menu_more
    };
    private String[] linkUrl=new String[]{
            "ywcai.ls.mobileutil.tools.Ping.view.PingActivity",
            "ywcai.ls.mobileutil.tools.TestActivity",
            "ywcai.ls.mobileutil.tools.TestActivity",
            "ywcai.ls.mobileutil.tools.TestActivity",
            "ywcai.ls.mobileutil.tools.TestActivity",
            "ywcai.ls.mobileutil.tools.TestActivity",
            "ywcai.ls.mobileutil.tools.TestActivity",
            "ywcai.ls.mobileutil.tools.TestActivity",
            "ywcai.ls.mobileutil.tools.TestActivity",
            "ywcai.ls.mobileutil.tools.TestActivity"
    };
    private ActivityRouter activityRouter=new ActivityRouter();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LsLog.saveLog("MenuFragment : 初始化Fragment view");
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        //安装网格化选择菜单
        InitView();
        InitMenu();
        InitToolBar();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        clickEnable=true;
        super.onResume();
    }

    private void InitToolBar() {
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        mToolbar.setTitle("让测试如此轻松");
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu1:
                        break;
                    case R.id.menu2:
                        break;
                    //打开选择应用菜单
                    case R.id.menu3:
                        break;
                    case R.id.menu4:
//                        cutProcess();
                        break;
                }
                return false;
            }
        });
    }

    private void InitView() {
        Glide.with(this).load(R.drawable
        .main_menu_top)
                .bitmapTransform(new BlurTransformation(this.getContext(), 10))
                .into((ImageView) view.findViewById(R.id.main_head_bg));
    }
    private void InitMenu() {

        for (int i=0;i<menus.length;i++) {
            IndexMenu indexMenu = new IndexMenu();
            indexMenu.menuText =menus[i];
            indexMenu.imgUrl= ContextCompat.getDrawable(this.getContext(),menuImg[i]);
            indexMenu.linkUrl=linkUrl[i];
            menuList.add(indexMenu);
        }
        menuAdapter=new MenuAdapter(this.getContext(),menuList);
        menuAdapter.setOnclickListener(new OnItemClickListener() {
            @Override
            public void OnClickItem(View v, int pos) {
                if(clickEnable) {
                    //放置双击打开两个activity;
                    clickEnable=false;
                    activityRouter.startActivity(null,menuList.get(pos).linkUrl);
                }
            }
        });
        recyclerView=(RecyclerView)view.findViewById(R.id.menus);
        recyclerView.setAdapter(menuAdapter);
        GridLayoutManager layoutManager = new
                GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        MenuItemDecoration menuItemDecoration=new MenuItemDecoration();
        recyclerView.addItemDecoration(menuItemDecoration);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDeviceList(GlobalEvent event) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onDestroy() {
        Log.i("ywcai", "onDestroy: ");
        super.onDestroy();
    }

    private void showToast(String tip) {
        Toast.makeText(this.getContext(), tip, Toast.LENGTH_LONG).show();
    }
}
