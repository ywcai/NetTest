package ywcai.ls.mobileutil.global.util.statics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;

/**
 * Created by zmy_11 on 2018/2/21.
 */

public class LsToolbar {
    public static void initToolbar(final Activity activity, String title, boolean isShowBack, View.OnClickListener onClickListener, int colorRes) {
        TextView titleView = (TextView) activity.findViewById(R.id.ls_toolbar_title);
        titleView.setText(title);
        TextView btnBack = (TextView) activity.findViewById(R.id.ls_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        TextView btnMore = (TextView) activity.findViewById(R.id.ls_toolbar_more);
        if (onClickListener == null) {
            btnMore.setVisibility(View.INVISIBLE);
        } else {
            btnMore.setVisibility(View.VISIBLE);
            btnMore.setOnClickListener(onClickListener);
        }
        if (!isShowBack) {
            btnBack.setVisibility(View.GONE);
        }
        if (colorRes != 0) {
            btnBack.setTextColor(ContextCompat.getColor(MainApplication.getInstance().getApplicationContext(), colorRes));
            btnMore.setTextColor(ContextCompat.getColor(MainApplication.getInstance().getApplicationContext(), colorRes));
            titleView.setTextColor(ContextCompat.getColor(MainApplication.getInstance().getApplicationContext(), colorRes));
        }
    }

    public static void initToolbar(Activity activity, String title) {
        initToolbar(activity, title, true, null, 0);
    }
}
