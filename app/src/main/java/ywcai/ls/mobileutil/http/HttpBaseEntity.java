package ywcai.ls.mobileutil.http;

import com.google.gson.annotations.SerializedName;


public class HttpBaseEntity<T> {

    @SerializedName("code")
    public int code = 0;
    @SerializedName("msg")
    public String msg = "unknown";
    @SerializedName("data")
    public T data;

    public boolean isSuccess() {
        return code == 0;
    }

}
