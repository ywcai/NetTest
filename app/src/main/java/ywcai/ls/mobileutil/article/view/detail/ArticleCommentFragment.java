package ywcai.ls.mobileutil.article.view.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.article.model.ArticleIndex;
import ywcai.ls.mobileutil.article.model.CommentListAdapter;
import ywcai.ls.mobileutil.article.model.UserComment;
import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.ObservableTransformer;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.smooth.tip.SmoothTip;


public class ArticleCommentFragment extends Fragment {
    View view;
    private ArticleIndex articleIndex;
    public long startID = -1, endID = -1;
    int pageSize = 20;//startID是最新的，endID是最老的
    CommentListAdapter commentListAdapter;
    List<UserComment> list;
    public PullLoadMoreRecyclerView pullView;
    HttpService http;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_article_comment, container, false);
        InitView();
        pullView.refresh();
        return view;
    }

    private void InitView() {
        http = RetrofitFactory.getHttpService();
        list = new ArrayList();
        commentListAdapter = new CommentListAdapter(this.getContext(), list);
        pullView = (PullLoadMoreRecyclerView) view.findViewById(R.id.article_comment_list);
        pullView.setLinearLayout();
        pullView.setAdapter(commentListAdapter);


        pullView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadNewComment();
            }

            @Override
            public void onLoadMore() {
                loadOldComment();
            }
        });
    }

    private void loadNewComment() {
        http.getNewComment(articleIndex.articleId, startID)
                .compose(ObservableTransformer.schedulersTransformer())
                .subscribe(new BaseObserver<List<UserComment>>() {
                    @Override
                    protected void success(List<UserComment> _list) {
                        pullView.setPullLoadMoreCompleted();
                        list.addAll(0, _list);
                        commentListAdapter.notifyDataSetChanged();
                        if (startID == -1) {
                            //数据已经全读取了,禁止加载更多
                            if (_list.size() < 20) {
                                pullView.setPushRefreshEnable(false);
                            } else {
                                endID = _list.get(_list.size() - 1).id;
                            }
                        }
                        if (_list.size() > 0) {
                            showTopTip("发现" + _list.size() + "条最新评论!");
                            startID = _list.get(0).id;
                        }
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        pullView.setPullLoadMoreCompleted();
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        pullView.setPullLoadMoreCompleted();
                        showTopTip("网络异常，请稍后重试");
                    }
                });
    }

    public void loadOldComment() {
        http.getOldComment(articleIndex.articleId, endID)
                .compose(ObservableTransformer.schedulersTransformer())
                .subscribe(new BaseObserver<List<UserComment>>() {
                    @Override
                    protected void success(List<UserComment> _list) {
                        pullView.setPullLoadMoreCompleted();
                        list.addAll(_list);
                        commentListAdapter.notifyDataSetChanged();
                        if (_list.size() <=0) {
                            pullView.setPushRefreshEnable(false);
                            showBottomTip("已经撸到底了");
                        }
                        else
                        {
                            endID = _list.get(_list.size() - 1).id;
                        }

                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        pullView.setPullLoadMoreCompleted();
//                        showBottomTip(msg.toString());
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        pullView.setPullLoadMoreCompleted();
                        showBottomTip("网络异常，请稍后重试");
                    }
                });
    }


    public void setArticleIndex(ArticleIndex articleIndex) {
        this.articleIndex = articleIndex;
    }

    public void showBottomTip(String tip) {

        RelativeLayout bottomTip = (RelativeLayout) view.findViewById(R.id.article_comment_tip_bottom);
        LsSnack.show(this.getContext(), bottomTip, tip);
    }

    public void showTopTip(String tip) {
        SmoothTip topTip = (SmoothTip) view.findViewById(R.id.article_comment_tip_top);
        topTip.showTip(tip, 1000, SmoothTip.SMOOTH_ORIENTATION_TOP);
    }
}
