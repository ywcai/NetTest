package ywcai.ls.mobileutil.http;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import ywcai.ls.mobileutil.login.model.MyUser;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.setting.LogEntity;


public interface HttpService {
    @GET("NetTest/user/get/{openID}/{channelID}")
    Observable<HttpBaseEntity<MyUser>> login(
            @Path("openID") String openID,
            @Path("channelID") int channelID);

    @POST("NetTest/user/put/nickname/{userid}")
    Observable<HttpBaseEntity<MyUser>> postNickname(
            @Path("userid") long userid,
            @Query("nickname") String nickname);


    @POST("NetTest/record/post/single/{userid}")
    Observable<HttpBaseEntity<UploadResult>> addRecord(
            @Path("userid") long userid,
            @Body LogEntity logEntity);


    @POST("NetTest/record/post/list/{userid}")
    Observable<HttpBaseEntity<UploadResult>> postRecords(
            @Path("userid") long userid,
            @Body List<LogEntity> logs);


    @GET("NetTest/record/get/list/{userid}")
    Observable<HttpBaseEntity<List<LogIndex>>> getLogList(@Path("userid") long userid);

    @POST("NetTest/record/get/{userid}/pos/{pos}")
    Observable<HttpBaseEntity<LogEntity>> loadRemoteDetailRecord(
            @Path("userid") long userid,
            @Path("pos") int pos,
            @Body ResultState resultState);

    @POST("NetTest/record/put/aliasname/{userid}/{recordid}")
    Observable<HttpBaseEntity<LogEntity>> editRecordName(
            @Path("userid") long userid,
            @Path("recordid") long recordid,
            @Query("aliasname") String aliasname);

    @POST("NetTest/record/del/{userid}/{recordid}/{pos}")
    Observable<HttpBaseEntity<LogEntity>> delRecordForId(
            @Path("userid") long userid,
            @Path("recordid") long recordid,
            @Path("pos") int pos,
            @Body ResultState resultState);

//    @POST("NetTest/record/down/{userid}/{recordid}/{pos}")
//    Observable<HttpBaseEntity<LogEntity>> downloadRecordForId(
//            @Path("userid") long userid,
//            @Path("recordid") long recordid,
//            @Path("recordid") int pos,
//            @Body ResultState resultState);
}
