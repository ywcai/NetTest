package ywcai.ls.mobileutil.tools.Speed.model.inf;

        import okhttp3.ResponseBody;
        import retrofit2.Call;
        import retrofit2.http.GET;
        import retrofit2.http.Url;


public interface DownService {
    @GET
    Call<ResponseBody> getApk(@Url String url);
}
