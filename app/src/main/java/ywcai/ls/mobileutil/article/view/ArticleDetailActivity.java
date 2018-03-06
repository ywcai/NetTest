package ywcai.ls.mobileutil.article.view;

import android.graphics.Color;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.drakeet.materialdialog.MaterialDialog;
import mehdi.sakout.fancybuttons.FancyButton;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ywcai.ls.control.LoadingDialog;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.article.model.ArticleDetailPageAdapter;
import ywcai.ls.mobileutil.article.model.ArticleIndex;
import ywcai.ls.mobileutil.article.model.UserComment;
import ywcai.ls.mobileutil.article.model.inf.CallBackUpdate;
import ywcai.ls.mobileutil.article.view.detail.ArticleCommentFragment;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;

import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.global.util.statics.LsSnack;
import ywcai.ls.mobileutil.global.util.statics.LsToolbar;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;
import ywcai.ls.mobileutil.http.BaseObserver;


import ywcai.ls.mobileutil.http.HttpService;
import ywcai.ls.mobileutil.http.ObservableTransformer;
import ywcai.ls.mobileutil.http.RetrofitFactory;
import ywcai.ls.mobileutil.http.UploadResult;


@Route(path = "/article/view/sub.ArticleDetailActivity")
public class ArticleDetailActivity extends AppCompatActivity {

    public String titleName = "正文";
    @Autowired()
    public int selectPos = 0;//只有0和1的选项，默认为0
    @Autowired()
    public String strArticleIndex;
    TextView titleView, commentView, topView, btnOperator, btnPopEditView;
    ViewPager viewPager;
    ArticleIndex articleIndex;
    ImageView imgBtnPopEditView;
    View bootView;
    BottomSheetDialog bottomSheetDialog;
    CacheProcess cacheProcess = CacheProcess.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        SetTitle.setTitleTransparent(getWindow());//沉静式状态栏
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_article_detail);
        InitView();
        InitToolBar();
        InitFragment();
        loadPageIndex();
    }

    private void InitView() {
        Gson gson = new Gson();
        try {
            articleIndex = gson.fromJson(strArticleIndex, ArticleIndex.class);
        } catch (Exception e) {
            articleIndex = new ArticleIndex();
        }
        imgBtnPopEditView = (ImageView) findViewById(R.id.article_detail_pop_edit_img);
        btnPopEditView = (TextView) findViewById(R.id.article_detail_bottom_pop_edit);
        imgBtnPopEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGuideWindow();
            }
        });
        btnPopEditView.setClickable(true);
        btnPopEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGuideWindow();
            }
        });
        titleView = (TextView) findViewById(R.id.ls_toolbar_title);
        viewPager = (ViewPager) findViewById(R.id.article_detail_pager);
        commentView = (TextView) findViewById(R.id.article_detail_comment_number);
        topView = (TextView) findViewById(R.id.article_detail_top_number);
        btnOperator = (TextView) findViewById(R.id.article_detail_pop_operator);
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setArticleTop();
            }
        });

        btnOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPos = selectPos == 0 ? 1 : 0;
                loadPageIndex();
            }
        });
        commentView.setText(articleIndex.comment + "");
        topView.setText(articleIndex.top + "");
        bootView = LayoutInflater.from(this)
                .inflate(R.layout.pop_bottom_dialog_input, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(bootView);

        FancyButton post = (FancyButton) bootView.findViewById(R.id.user_input_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialEditText editText = (MaterialEditText) bootView.findViewById(R.id.user_input_box);
                String commentContent = editText.getText().toString();
                if (commentContent.length() < 5 || commentContent.length() > 300) {
                    return;
                }
                bottomSheetDialog.hide();
                final LoadingDialog loadingDialog = new LoadingDialog(ArticleDetailActivity.this);
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
                final UserComment userComment = new UserComment();


                userComment.setData(articleIndex.articleId, commentContent, new CallBackUpdate() {
                    @Override
                    public void updateComment() {
                        final HttpService httpService = RetrofitFactory.getHttpService();
                        httpService.postComment(userComment)
                                .throttleFirst(3000, TimeUnit.MILLISECONDS)
                                .compose(ObservableTransformer.schedulersTransformer())
                                .subscribe(new BaseObserver<UploadResult>() {
                                    @Override
                                    protected void success(UploadResult o) {
                                        loadingDialog.dismiss();
                                        updateCommentUi(o);
                                    }

                                    @Override
                                    protected void onCodeError(int code, String msg) {
                                        loadingDialog.dismiss();
                                        showSnackTip(msg, false);
                                    }

                                    @Override
                                    protected void onNetError(Throwable e) {
                                        loadingDialog.dismiss();
                                        showSnackTip(e.toString(), false);
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateCommentUi(UploadResult o) {
        //先更新列表，在切换到值comment页面
        commentView.setText(o.uploadSize + "");
        ArticleCommentFragment comment = (ArticleCommentFragment) ((ArticleDetailPageAdapter) viewPager.getAdapter()).pageList.get(1);
        comment.pullView.refresh();
        selectPos = 1;
        loadPageIndex();
    }

    private void showSnackTip(String tip, Boolean success) {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.article_detail_snack_container);
        if (!success) {
            LsSnack.show(this, rl, tip, R.color.LRed);
        } else {
            LsSnack.show(this, rl, tip);
        }
    }

    private void showGuideWindow() {
        if (cacheProcess.getCacheUser() == null) {
            final MaterialDialog materialDialog = new MaterialDialog(ArticleDetailActivity.this);
            materialDialog.setTitle(null);
            materialDialog.setMessage("请先登录");
            materialDialog.setCanceledOnTouchOutside(true);
            materialDialog.setPositiveButton("前往登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDialog.dismiss();
                    ARouter.getInstance().build(AppConfig.LOGIN_ACTIVITY_PATH)
                            .navigation();
                }
            });
            materialDialog.setNegativeButton("暂时不用", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDialog.dismiss();
                }
            });
            materialDialog.show();
            return;
        }
        bottomSheetDialog.show();
    }

    //获取相邻的文章记录
    private void InitFragment() {
        ArticleDetailPageAdapter adapter = new ArticleDetailPageAdapter(this.getSupportFragmentManager(), articleIndex);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPos = position;
                loadPageIndex();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateOrientation(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.article_detail_scroll_y:
                changeTitleView((boolean) (event.obj));
                break;
        }
    }

    private void changeTitleView(boolean scrollOrientation) {
        //向上滑动
        if (!scrollOrientation) {
            titleName = articleIndex.title;
        } else {
            titleName = "正文";
        }
        titleView.setText(titleName);
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(500);
        titleView.setAnimation(animation);
        animation.start();
    }

    private void loadPageIndex() {
        if (selectPos == 0) {
            selectArticleContent();
        } else {
            selectArticleComment();
        }
        viewPager.setCurrentItem(selectPos);
    }

    private void selectArticleComment() {
        titleView.setText("评论");
        btnOperator.setText("看正文");
        commentView.setVisibility(View.INVISIBLE);
    }

    private void selectArticleContent() {
        titleView.setText(titleName);
        btnOperator.setText("看评论");
        commentView.setVisibility(View.VISIBLE);
    }


    //点赞
    private void setArticleTop() {

    }

    private void InitToolBar() {
        LsToolbar.initToolbar(this, "");
    }
}
