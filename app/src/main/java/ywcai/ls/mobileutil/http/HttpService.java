package ywcai.ls.mobileutil.http;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import ywcai.ls.mobileutil.article.model.ArticleIndex;
import ywcai.ls.mobileutil.article.model.UserComment;
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

    @POST("NetTest/record/del/{userid}")
    Observable<HttpBaseEntity<List<LogEntity>>> delRecords(
            @Path("userid") long userid);

    @GET("NetTest/article/get/new/{type}/{size}/{startId}")
    Observable<HttpBaseEntity<List<ArticleIndex>>> getNewArticleList(
            @Path("type") int type,
            @Path("size") int size,
            @Path("startId") long startId);

    @GET("NetTest/article/get/old/{type}/{size}/{endId}")
    Observable<HttpBaseEntity<List<ArticleIndex>>> getOldArticleList(
            @Path("type") int type,
            @Path("size") int size,
            @Path("endId") long endId);


    @GET("NetTest/article/get/near/{type}/{size}/{centerId}")
    Observable<HttpBaseEntity<List<ArticleIndex>>> getNearArticleList(
            @Path("type") int type,
            @Path("size") int size,
            @Path("centerId") long centerId);

    @POST("NetTest/article/post/comment")
    Observable<HttpBaseEntity<UploadResult>> postComment(@Body UserComment userComment);

    @GET("NetTest/article/get/new/comment/{articleId}/{startId}")
    Observable<HttpBaseEntity<List<UserComment>>> getNewComment(
            @Path("articleId") long articleId,
            @Path("startId") long startId);

    //第一次获取数据，按照20个获取
    @GET("NetTest/article/get/great/comment/{articleId}")
    Observable<HttpBaseEntity<List<UserComment>>> getGreatComment(
            @Path("articleId") long articleId);

    //第一次获取数据，按照20个获取
    @GET("NetTest/article/get/old/comment/{articleId}/{endId}")
    Observable<HttpBaseEntity<List<UserComment>>> getOldComment(
            @Path("articleId") long articleId,
            @Path("endId") long endId
    );
}
