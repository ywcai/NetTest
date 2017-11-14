package ywcai.ls.mobileutil.service;

import android.app.Service;
import android.os.Binder;

public class MyBinder extends Binder {
    private Service service;

    public Service getService() {
        return service;
    }
    public void setService(Service service) {
        this.service = service;
    }
}
