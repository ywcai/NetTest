package ywcai.ls.mobileutil.global.presenter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

import ywcai.ls.mobileutil.global.model.HookUtil;
import ywcai.ls.mobileutil.global.util.statics.LsLog;

/**
 * Created by zmy_11 on 2017/10/6.
 */

@Interceptor(priority = 88, name = "global hook")
public class GlobalInterceptor implements IInterceptor {
    HookUtil hookUtil;
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
//        setHookClass(postcard.getPath());
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
        //拦截器初始化方法，仅仅会在第一次初始化时调用一次
//        hookUtil =new HookUtil(context);
    }
    public void setHookClass(String path) {
        Class<?> clazz= null;
        String className="ywcai.ls.mobileutil"+path.replace("/",".");
        try {
            clazz=Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(clazz!=null)
        {
            hookUtil.hookAms(clazz,path);
        }
    }
}
