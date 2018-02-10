package ywcai.ls.mobileutil.http;

/**
 * Created by zmy_11 on 2018/2/6.
 */

public class UploadResult {
    public String msg="";
    public int uploadSize=0;

    @Override
    public String toString() {
        return "UploadResult{" +
                "msg='" + msg + '\'' +
                ", uploadSize=" + uploadSize +
                '}';
    }
}
