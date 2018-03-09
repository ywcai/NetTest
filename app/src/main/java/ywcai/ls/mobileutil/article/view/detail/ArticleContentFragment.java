package ywcai.ls.mobileutil.article.view.detail;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.article.model.ArticleIndex;
import ywcai.ls.mobileutil.article.model.ArticleNearListAdapter;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.http.BaseObserver;
import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.ObservableTransformer;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.menu.presenter.inf.OnItemClickListener;


/**
 * Created by zmy_11 on 2018/2/21.
 */

public class ArticleContentFragment extends Fragment {
    View view;
    WebView htmlTextView;
    private ArticleIndex articleIndex;
    TextView titleText, authText, pvText, createTimeText, remarksText;
    ImageView thumbView;
    RecyclerView nearListView;
    List nearList = new ArrayList<ArticleIndex>();
    ArticleNearListAdapter nearListAdapter;
    HttpService httpService;
    private boolean scrollOrientation = false;//false代表当前可检测向上滑动

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_article_content, container, false);
        InitView();
        loadNearList();
        return view;
    }

    private void InitView() {
        httpService = RetrofitFactory.getHttpService();
        htmlTextView = (WebView) view.findViewById(R.id.article_detail_page_content);
        titleText = (TextView) view.findViewById(R.id.article_content_title);
        authText = (TextView) view.findViewById(R.id.article_content_auth);
        pvText = (TextView) view.findViewById(R.id.article_content_pv);
        createTimeText = (TextView) view.findViewById(R.id.article_content_create_time);
        remarksText = (TextView) view.findViewById(R.id.article_content_remarks);
        thumbView = (ImageView) view.findViewById(R.id.article_content_thumb);
        WebSettings ws = htmlTextView.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.setDefaultTextEncodingName("UTF-8");
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setUseWideViewPort(true);
        htmlTextView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (Build.VERSION.SDK_INT < 19) {
                    htmlTextView.loadUrl("javascript:setImgWidth()");
                } else {
                    htmlTextView.evaluateJavascript("javascript:setImgWidth()", null);
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });

        nearListView = (RecyclerView) view.findViewById(R.id.article_detail_page_content_link);
        nearListAdapter = new ArticleNearListAdapter(this.getContext(), nearList);
        nearListView.setAdapter(nearListAdapter);
        LinearLayoutManager ly = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        nearListView.setLayoutManager(ly);
        nearListAdapter.setOnclickListener(new OnItemClickListener() {
            @Override
            public void OnClickItem(View v, int pos) {
                Gson gson = new Gson();
                String articleIndex = gson.toJson(nearList.get(pos));
                ARouter.getInstance().build(AppConfig.ARTICLE_DETAIL_ACTIVITY_PATH)
                        .withInt("selectPos", 0)
                        .withString("strArticleIndex", articleIndex)
                        .navigation();
            }
        });

        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!scrollOrientation) {
                    if (scrollView.getScrollY() > (titleText.getTop() + titleText.getHeight())) {
                        //触发向上滑过界事件
                        MsgHelper.sendStickEvent(GlobalEventT.article_detail_scroll_y, "", false);
                        scrollOrientation = true;
                    }
                }
                //方下拉
                else {
                    if (scrollView.getScrollY() < (titleText.getTop() + titleText.getHeight())) {
                        MsgHelper.sendStickEvent(GlobalEventT.article_detail_scroll_y, "", true);
                        scrollOrientation = false;
                        //触发向下事件
                    }
                }
            }
        });
    }

    private void loadNearList() {
        httpService.getNearArticleList(articleIndex.articleType, 2, articleIndex.articleId)
                .delay(200, TimeUnit.MILLISECONDS)
                .compose(ObservableTransformer.schedulersTransformer())
                .subscribe(new BaseObserver<List<ArticleIndex>>() {
                    @Override
                    protected void success(List<ArticleIndex> o) {
                        updateContent(o.get(0));
                        nearList.clear();
                        o.remove(0);
                        nearList.addAll(o);
                        nearListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onCodeError(int code, String msg) {
                        htmlTextView.loadDataWithBaseURL(null, "服务端数据异常，请稍后重试！", "text/html", "UTF-8", null);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        htmlTextView.loadDataWithBaseURL(null, "网络连接失败，请稍后重试！", "text/html", "UTF-8", null);
                    }
                });
    }


    private void updateContent(ArticleIndex articleIndex) {
        titleText.setText(articleIndex.title);
        authText.setText(articleIndex.authNickname);
        pvText.setText(articleIndex.pv + "访问量");
        remarksText.setText(articleIndex.remarks);
        createTimeText.setText(articleIndex.createTime);
        String meta = "<html>" +
                "<meta name='viewport' content='width=device-width,target-densitydpi=high-dpi,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no'/>"+
                "<head>";
        meta = meta +
                "<script type='text/javascript'> " +
                "function setImgWidth() {" +
                "   for(var i=0;i<document.images.length;i++) " +
                "    { " +
                "      if(document.images[i].width>=document.body.clientWidth) " +
                "      { " +
                "      document.images[i].style.width='98%'; " +
                "      } " +
                "    } " +
                "  } " +
                "</script></head>";
        String textSizeStyleStart = "<body style='padding:0px;margin:0px;overflow:scroll;font-size:14px;word-break:break-all'>";
        String divTagEnd = "</body></html>";
        htmlTextView.loadDataWithBaseURL(null, meta + textSizeStyleStart + articleIndex.articleContent + divTagEnd, "text/html", "UTF-8", null);
    }

    public void setArticleIndex(ArticleIndex articleIndex) {
        this.articleIndex = articleIndex;
    }
}
