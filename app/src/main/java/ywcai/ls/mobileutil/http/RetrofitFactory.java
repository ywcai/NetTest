package ywcai.ls.mobileutil.http;


import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ywcai.ls.mobileutil.global.cfg.AppConfig;

public class RetrofitFactory {

    public static HttpService getHttpService() {
        OkHttpClient httpClient = new OkHttpClient.Builder().
                connectTimeout(3000, TimeUnit.SECONDS)//连接超时时间
                .build();
        HttpService service = new Retrofit.Builder()
                .baseUrl(AppConfig.HTTP_APP_CONFIG_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .build()
                .create(HttpService.class);
        return service;
    }
}
