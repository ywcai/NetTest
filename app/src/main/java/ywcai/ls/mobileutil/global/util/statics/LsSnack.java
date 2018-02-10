package ywcai.ls.mobileutil.global.util.statics;

import android.content.Context;
import android.graphics.Color;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;

public class LsSnack {

    public static void show(Context context, final View snack_container, String tip, int bgColor) {
        final Snackbar snackbar = Snackbar.make(snack_container, tip, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView textView = ((TextView) view.findViewById(R.id.snackbar_text));
        textView.setTextColor(Color.WHITE);
        if (bgColor == -1) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.tipSuccess));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, bgColor));
        }
        snackbar.show();
    }

    public static void show(Context context, View snack_container, String tip) {
        show(context, snack_container, tip, -1);
    }
}
