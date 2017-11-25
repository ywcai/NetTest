package ywcai.ls.mobileutil.global.util.statics;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;

/**
 * Created by zmy_11 on 2017/11/25.
 */

public class LsSnack {

    public static void show(Context context, View snack_container, String tip, int bgColor) {
        Snackbar snackbar = Snackbar.make(snack_container, tip, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
        if (bgColor == -1) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.tipSuccess));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, bgColor));
        }
        snackbar.show();
    }
    public static void show(Context context, View snack_container, String tip) {
        show(context,snack_container,tip,-1);
    }
}
