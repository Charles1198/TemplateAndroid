package com.charles.common.network;

import com.charles.common.Constant;
import com.charles.common.error.model.ApiErrorInfo;
import com.charles.common.kv.Kv;
import com.charles.common.network.response.BaseResp;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author charles
 * @date 2018/10/31
 * @description
 */
public class SubmitError {
    /**
     * 发送设备崩溃信息
     */
    public static void sendException() {
        if (!NetworkUtil.isNetworkConnected()) {
            return;
        }

        String exceptionJsonString = Kv.getString(Constant.FILE_CRASH);
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                exceptionJsonString);
        ApiManager apiManager = NetworkUtil.createApiManager();
        Call<BaseResp> call = apiManager.sendException(requestBody);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                Kv.remove(Constant.FILE_CRASH);
            }

            @Override
            public void onFailure(Call<BaseResp> call, Throwable t) {
            }
        });
    }

    /**
     * 发送接口异常信息
     */
    public static void sendApiError(ApiErrorInfo apiErrorInfo) {
        if (!NetworkUtil.isNetworkConnected()) {
            return;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                apiErrorInfo.toJsonString());
        ApiManager apiManager = NetworkUtil.createApiManager();
        Call<BaseResp> call = apiManager.sendApiError(requestBody);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                Kv.remove(Constant.FILE_CRASH);
            }

            @Override
            public void onFailure(Call<BaseResp> call, Throwable t) {

            }
        });
    }
}
