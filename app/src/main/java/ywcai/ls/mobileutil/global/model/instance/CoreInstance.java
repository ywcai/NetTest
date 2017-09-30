package ywcai.ls.mobileutil.global.model.instance;

import ywcai.ls.mobileutil.global.model.User;

public class CoreInstance {
    private static Object lock = new Object();
    public static CoreInstance socketObject = null;
    public User loginUser;
    private CoreInstance() {

    }
    public static CoreInstance getInstance() {
        synchronized (lock) {
            if (socketObject == null) {
                socketObject = new CoreInstance();
            }
        }
        return socketObject;
    }
}
