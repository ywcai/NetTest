package ywcai.ls.mobileutil.global.model;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zmy_11 on 2017/10/8.
 */

public class HookUtil {
    private Class<?> proxyActivity;
    private Context context;
    public HookUtil(Context context) {
        this.context = context;
    }

    public void hookAms(Class<?> _proxyActivity, String path) {
        this.proxyActivity = _proxyActivity;
        //一路反射，直到拿到IActivityManager的对象
        try {
            Class<?> ActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            Field defaultFiled = ActivityManagerNativeClass.getDeclaredField("gDefault");
            defaultFiled.setAccessible(true);
            //静态字段，所以传入null没问题，如果不是静态字段，则不能传入null
            Object defaultValue = defaultFiled.get(null);
            //反射SingleTon
            Class<?> SingletonClass = Class.forName("android.util.Singleton");
            Field mInstance = SingletonClass.getDeclaredField("mInstance");
            mInstance.setAccessible(true);
            //到这里已经拿到ActivityManager对象
            Object iActivityManagerObject = mInstance.get(defaultValue);
            //开始动态代理，用代理对象替换掉真实的ActivityManager，瞒天过海
            Class<?> IActivityManagerIntercept = Class.forName("android.app.IActivityManager");
            //具体替换的实现，参数为要替换的对象。
            AmsInvocationHandler handler = new AmsInvocationHandler(iActivityManagerObject, path);
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{IActivityManagerIntercept}, handler);
            //现在替换掉这个对象
            mInstance.set(defaultValue, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    class AmsInvocationHandler implements InvocationHandler {
        private Object iActivityManagerObject;
        private String path;

        public AmsInvocationHandler(Object iActivityManagerObject, String path) {
            this.iActivityManagerObject = iActivityManagerObject;
            this.path = path;

        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            saveTopActivityCache(method);
            return method.invoke(iActivityManagerObject, args);
        }

        private void saveTopActivityCache(Method method) {
            //// TODO: 2017/10/8 暂时没啥需要埋的
        }
    }
}

