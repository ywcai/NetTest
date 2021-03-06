package ywcai.ls.mobileutil.web;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.daimajia.numberprogressbar.NumberProgressBar;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.util.statics.LsToolbar;
import ywcai.ls.mobileutil.global.util.statics.SetTitle;


@Route(path = "/web/WebActivity")
public class WebActivity extends AppCompatActivity {
    @Autowired()
    public String httpUrl, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        SetTitle.setTitleTransparent(getWindow());
        setContentView(R.layout.activity_web);
        LsToolbar.initToolbar(this, title);
        loadWeb();
    }

    public void loadWeb() {
        final NumberProgressBar bar = (NumberProgressBar) findViewById(R.id.web_progress);
        WebView webView = (WebView) findViewById(R.id.web_container);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                bar.setVisibility(View.VISIBLE);
                bar.setProgress(progress);
                if (progress == 100) {
                    bar.setProgress(0);
                    bar.setVisibility(View.INVISIBLE);
                }
            }
        });
        webView.loadUrl(httpUrl);
    }
}
