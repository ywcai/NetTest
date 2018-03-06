package ywcai.ls.smooth.tip;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zmy_11 on 2018/2/13.
 */

public class SmoothTip extends RelativeLayout {
    private Context context;
    public TextView textView;
    public static final int SMOOTH_ORIENTATION_TOP = 0;
    public static final int SMOOTH_ORIENTATION_BOT = 1;

    public SmoothTip(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SmoothTip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SmoothTip(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SmoothTip(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context _context, @Nullable AttributeSet attrs, int defStyleAttr) {
        context = _context;
        LayoutInflater.from(context).inflate(R.layout.smooth_tip, this, true);
        textView = (TextView) findViewById(R.id.smooth_text);
        this.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void showTip(String tip, int milliSeconds, int orientation) {
        textView.setText(tip);
        Observable.just(orientation)
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer orientation) {
                        inAnimation(orientation);
                        return Observable.just(orientation);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .delay(milliSeconds + 300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer orientation) {
                        outAnimation(orientation);
                    }
                });
    }

    //方向0代表自上而下，1代表自下而上
    private void inAnimation(Integer orientation) {
        Animation animation = null;
        float top, bottom, h;
        top = SmoothTip.this.getTop();
        bottom = SmoothTip.this.getBottom();
        h = SmoothTip.this.getHeight();
        if (orientation == SMOOTH_ORIENTATION_TOP) {
            animation = new TranslateAnimation(0, 0, top - h, top);
        }
        if (orientation == SMOOTH_ORIENTATION_BOT) {
            animation = new AlphaAnimation(0,1);
        }
        animation.setInterpolator(new DecelerateInterpolator());//前块后慢
        animation.setDuration(300);
        SmoothTip.this.setVisibility(View.VISIBLE);
        SmoothTip.this.startAnimation(animation);

    }

    private void outAnimation(Integer orientation) {
        Animation animation = null;
        float top, bottom, h;
        top = SmoothTip.this.getTop();
        bottom = SmoothTip.this.getBottom();
        h = SmoothTip.this.getHeight();
        if (orientation == SMOOTH_ORIENTATION_TOP) {
            animation = new TranslateAnimation(0, 0, top, top - h);
        }
        if (orientation == SMOOTH_ORIENTATION_BOT) {
            animation = new AlphaAnimation(1,0);
        }
        animation.setInterpolator(new AccelerateDecelerateInterpolator());//前慢后块
        animation.setDuration(300);
        SmoothTip.this.startAnimation(animation);
        SmoothTip.this.setVisibility(View.INVISIBLE);
    }

}
