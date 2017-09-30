package ywcai.ls.mobileutil.global.presenter;

import android.app.Activity;
import android.content.Intent;

import ywcai.ls.mobileutil.global.presenter.inf.RouterInf;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;


public class ActivityRouter implements RouterInf {

//    private Activity activity;
//    public ActivityRouter(Activity activity) {
//        this.activity = activity;
//    }

    @Override
    public void startActivity(Activity activity,String packageName) {
        Intent intent = new Intent();
        Class clazz=null;
        try {
             clazz=Class.forName(packageName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(activity==null)
        {
            intent.setClass(MainApplication.getInstance().getApplicationContext(), clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainApplication.getInstance().getApplicationContext().startActivity(intent);
        }
        else
        {
            intent.setClass(activity, clazz);
            activity.startActivity(intent);
        }

    }
}
