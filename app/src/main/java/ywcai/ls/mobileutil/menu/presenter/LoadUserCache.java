package ywcai.ls.mobileutil.menu.presenter;
import ywcai.ls.mobileutil.global.model.instance.CoreInstance;
import ywcai.ls.mobileutil.global.presenter.CacheProcess;

public class LoadUserCache {
    private CacheProcess userCacheInf;
    public LoadUserCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                load();
            }
        }).start();
    }
    private void load() {
        userCacheInf=new CacheProcess();
        CoreInstance.getInstance().loginUser=userCacheInf.getCacheUser();
    }
}
