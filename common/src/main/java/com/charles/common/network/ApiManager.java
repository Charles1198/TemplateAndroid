package com.charles.common.network;

import com.charles.common.network.response.BaseResp;
import com.charles.common.network.response.FeedbackIdResp;
import com.charles.common.network.response.ServerResp;
import com.charles.common.network.response.TokenResp;
import com.charles.common.network.response.UUIDResp;
import com.charles.common.network.response.UpdateResp;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * @author charles
 * @date 16/9/29
 * 向服务器所传递的url的后半部分及相关的参数信息
 */

public interface ApiManager {

    /**
     * 检测服务器连通性
     * @return
     */
    @GET(".")
    Call<Void> connectServer();

    /**
     * 获取服务器列表
     *
     * @param shortName
     * @return
     */
    @GET("api/v1/server/config/{shortName}")
    Call<BaseResp<List<ServerResp>>> checkUrl(@Path("shortName") String shortName);

    /**
     * 检查版本更新
     *
     * @param shortName
     * @param platform
     * @param release
     * @return
     */
    @GET("api/v1/app/version/{shortName}/{platform}/{release}")
    Call<BaseResp<UpdateResp>> checkUpdate(@Path("shortName") String shortName,
                                           @Path("platform") String platform,
                                           @Path("release") String release);

    /**
     * 发送应用异常信息
     *
     * @param body
     * @return
     */
    @POST("api/v1/ticket/app-exception")
    Call<BaseResp> sendException(@Body RequestBody body);

    /**
     * 发送接口异常信息
     *
     * @param body
     * @return
     */
    @POST("api/v1/ticket/api-exception")
    Call<BaseResp> sendApiError(@Body RequestBody body);

    /**
     * 提交设备信息，获取UUID
     *
     * @param body
     * @return
     */
    @POST("api/v1/device/mobile-registration")
    Call<BaseResp<UUIDResp>> getUUID(@Body RequestBody body);

    /**
     * 应用激活
     *
     * @param body
     * @return
     */
    @POST("api/v1/app/registration")
    Call<BaseResp> registerApp(@Body RequestBody body);

    /**
     * 提交反馈内容
     *
     * @param body
     * @return
     */
    @POST("api/v1/ticket/app-feedback")
    Call<BaseResp<FeedbackIdResp>> submitFeedbackContent(@Body RequestBody body);

    /**
     * 提交反馈截图
     *
     * @param appFeedbackUUID
     * @param body
     * @return
     */
    @Headers("Content-Type: image/jpeg")
    @POST("api/v1/ticket/app-feedback/{appFeedbackUUID}/image")
    Call<BaseResp> submitFeedbackImage(@Path("appFeedbackUUID") String appFeedbackUUID, @Body RequestBody body);

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("initialization")
    Call<BaseResp<TokenResp>> checkToken(@Field("token") String token);

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    @FormUrlEncoded
    @POST("api/token")
    Call<BaseResp<TokenResp>> resetToken(@Field("refreshToken") String refreshToken);
}
