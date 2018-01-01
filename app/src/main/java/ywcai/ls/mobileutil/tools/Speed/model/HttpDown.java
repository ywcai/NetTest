package ywcai.ls.mobileutil.tools.Speed.model;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.tools.Speed.model.inf.DownService;

public class HttpDown {
    SpeedState speedState;
    CacheProcess cacheProcess = CacheProcess.getInstance();
    DownService service;


    public HttpDown(SpeedState speedState, DownService service) {
        this.speedState = speedState;
        this.service = service;
    }

    public void executeDownload(int index) {
        Call<ResponseBody> call = service.getApk(AppConfig.HTTP_TEST_URLS[index]);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processResult(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                processErr(t);
            }
        });

    }

    public void executeRead(int index) {
        Call<ResponseBody> call = service.getApk(AppConfig.HTTP_READ_URLS[index]);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processResult(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                processErr(t);
            }
        });
    }

    private void processErr(Throwable t) {
        //处理本次测试状态，包括结果数据
        synchronized (speedState) {
            speedState.currentComplete++;
            if (speedState.currentComplete >= (speedState.readMaxCount + speedState.downMaxCount)) {
                speedState.running = 2;

            }
            cacheProcess.setSpeedState(speedState);
        }
    }

    private void processResult(Response<ResponseBody> response) {
        synchronized (speedState) {
            //处理本次测试状态，包括结果数据
            long size = 0;
            try {
                size = response.body().bytes().length;
            } catch (Exception e) {
                e.printStackTrace();
            }
            speedState.payloadSize += size;
            speedState.currentComplete++;
            if (speedState.currentComplete >= (speedState.readMaxCount + speedState.downMaxCount)) {
                speedState.running = 2;
            }
            cacheProcess.setSpeedState(speedState);
        }
    }
}
