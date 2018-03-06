package ywcai.ls.mobileutil.article.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.article.model.ArticleAdapter;
import ywcai.ls.mobileutil.article.model.ArticleIndex;
import ywcai.ls.mobileutil.global.cfg.AppConfig;

import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.ObservableTransformer;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;
import ywcai.ls.smooth.tip.SmoothTip;

public class ArticleListFragment extends Fragment {
    View view;
    int type = 0;
    public boolean isExist = false;
    long startID = -1, endID = -1;
    int pageSize = 20;//startID是最新的，endID是最老的
    HttpService httpService;
    public PullLoadMoreRecyclerView listView;
    List<ArticleIndex> list;
    ArticleAdapter articleAdapter;
    SmoothTip smoothTipTop;
    RelativeLayout smoothTipBottom;
    private int pageIndex = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        httpService = RetrofitFactory.getHttpService();
        view = inflater.inflate(R.layout.fragment_main_article_sub, container, false);
        smoothTipTop = (SmoothTip) view.findViewById(R.id.tip_top);
        smoothTipBottom = (RelativeLayout) view.findViewById(R.id.tip_bottom);
        initListView();
        //如果是加载的第一页Fragment，则自动初始化数据，其他页则需要选择后加载数据
        if (pageIndex == 0) {
            pullLoad();
        }
        return view;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    private void initListView() {
        list = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this.getContext(), list);
        articleAdapter.setOnclickListener(new OnItemClickListener() {
            @Override
            public void OnClickItem(View v, int pos) {
                //点击主页，做成pager,选择新闻页
                Gson gson = new Gson();
                String articleIndex = "";
                try {
                    articleIndex = gson.toJson(list.get(pos));
                } catch (Exception e) {

                }
                ARouter.getInstance().build(AppConfig.ARTICLE_DETAIL_ACTIVITY_PATH)
                        .withInt("selectPos", 0)
                        .withString("strArticleIndex", articleIndex)
                        .navigation();
            }
        });
        articleAdapter.setOnclickComment(new OnItemClickListener() {
            @Override
            public void OnClickItem(View v, int pos) {
                //点击评论页，做成pager,选择评论
                Gson gson = new Gson();
                String articleIndex = "";
                try {
                    articleIndex = gson.toJson(list.get(pos));
                } catch (Exception e) {

                }
                ARouter.getInstance().build(AppConfig.ARTICLE_DETAIL_ACTIVITY_PATH)
                        .withInt("selectPos", 1)
                        .withString("strArticleIndex", articleIndex)
                        .navigation();
            }
        });
        listView = (PullLoadMoreRecyclerView) view.findViewById(R.id.article_list);
        listView.setLinearLayout();
        listView.setAdapter(articleAdapter);
        listView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                pullLoad();
            }

            @Override
            public void onLoadMore() {
                upPullLoad();
            }
        });
    }


    //下拉或初始化
    public void pullLoad() {
        Observable.just(listView)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PullLoadMoreRecyclerView>() {
                    @Override
                    public void call(PullLoadMoreRecyclerView pullLoadMoreRecyclerView) {
                        pullLoadMoreRecyclerView.setRefreshing(true);
                    }
                });
        httpService.getNewArticleList(type, pageSize, startID)
                .subscribeOn(Schedulers.io())
                .compose(ObservableTransformer.schedulersTransformer())
                .subscribe(new BaseObserver<List<ArticleIndex>>() {
                    @Override
                    protected void success(List<ArticleIndex> list) {
                        if(list.size()>0)
                        {
                            smoothTipTop.showTip("发现" + list.size() + "条新内容", 1000, SmoothTip.SMOOTH_ORIENTATION_TOP);
                            updateNewData(list);
                        }
                        listView.setPullLoadMoreCompleted();
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
//                        smoothTipTop.showTip("没有获取到新的内容", 1000, SmoothTip.SMOOTH_ORIENTATION_TOP);
                        listView.setPullLoadMoreCompleted();
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        smoothTipTop.showTip("网络异常，请稍后重试", 1000, SmoothTip.SMOOTH_ORIENTATION_TOP);
                        listView.setPullLoadMoreCompleted();
                    }
                });
    }

    //上拉加载
    public void upPullLoad() {
        //如果还没有数据，则不能使用上拉加载
        if (!isExist) {
            return;
        }
        httpService.getOldArticleList(type, pageSize, endID)
                .compose(ObservableTransformer.schedulersTransformer())
                .subscribe(new BaseObserver<List<ArticleIndex>>() {
                    @Override
                    protected void success(List<ArticleIndex> list) {
//                        showBotTip("加载到" + list.size() + "条内容",true);
                        updateOldData(list);
                        listView.setPullLoadMoreCompleted();
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        listView.getRecyclerView().scrollToPosition(list.size() - 1);
                        listView.setPullLoadMoreCompleted();
//                        showBotTip(msg.toString(),false);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        listView.setPullLoadMoreCompleted();
                        showBotTip("网络异常，请稍后重试",false);
                    }
                });
    }


    private void updateNewData(List<ArticleIndex> _list) {
        //在前面增加
        list.addAll(0, _list);
        articleAdapter.notifyDataSetChanged();
        if (!isExist) {
            endID = _list.get(_list.size() - 1).articleId;
        }
        startID = _list.get(0).articleId;
        isExist = true;
        listView.setPullLoadMoreCompleted();
        //如果有新数据，但所有数据加载一起已经不超过一页，说明已经获取了最老的数据，停止上拉加载
        if (list.size() < pageSize && list.size() > 0) {
            listView.setPushRefreshEnable(false);
        }
    }

    private void updateOldData(List<ArticleIndex> _list) {
        list.addAll(_list);
        articleAdapter.notifyDataSetChanged();
        endID = _list.get(_list.size() - 1).articleId;
        //如果加载的数据不超过一页，说明已经获取到了最老的数据，停止上拉加载
        if (_list.size() < pageSize) {
            listView.setPushRefreshEnable(false);
            showBotTip("已经撸到底了",true);
        }
    }

    public void showBotTip(String tip,boolean success)
    {
        if(success)
        {
            LsSnack.show(this.getContext(),smoothTipBottom,tip );
        }
        else
        {
            LsSnack.show(this.getContext(),smoothTipBottom,tip,R.color.LRed);
        }

    }

}
