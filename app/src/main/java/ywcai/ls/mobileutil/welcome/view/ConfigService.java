package ywcai.ls.mobileutil.welcome.view;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface ConfigService {
    @GET
    Call<ResponseBody> getAppInfo(@Url String requestUrl);
}
