package com.charles.common.network;

import android.util.Log;

import com.charles.common.error.model.ApiErrorInfo;
import com.charles.common.network.response.BaseResp;
import com.charles.common.util.TokenUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author charles
 * @date 2018/9/30
 * @description
 */
public abstract class AbstractMyCallBack<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() == null) {
            onFail("");

            ApiErrorInfo apiErrorInfo = new ApiErrorInfo(call, response);
            SubmitError.sendApiError(apiErrorInfo);

            Log.e("onResponse()-noBody", apiErrorInfo.toJsonString());
            return;
        }

        BaseResp baseResp = (BaseResp) response.body();
        switch (baseResp.getStatusCode()) {
            case NetworkUtil.SUCCESS:
                onSuc(response.body());
                break;
            case "":
                TokenUtil.reset(succeed -> {
                    if (succeed) {
                        requestAgain();
                    }
                });
                break;
            default:
                onFail(baseResp.getMessage());

                ApiErrorInfo apiErrorInfo = new ApiErrorInfo(call, response);
                SubmitError.sendApiError(apiErrorInfo);
                Log.e("onResponse()", apiErrorInfo.toJsonString());
                break;
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFail("");

        ApiErrorInfo apiErrorInfo = new ApiErrorInfo(call, null);
        SubmitError.sendApiError(apiErrorInfo);

        Log.e("onFailure()", apiErrorInfo.toJsonString());
        Log.e("Throwable", t.getMessage());
    }

    /**
     * 请求成功回调
     *
     * @param response
     */
    public abstract void onSuc(T response);

    /**
     * 请求失败回调
     *
     * @param statusCode
     */
    public abstract void onFail(String statusCode);

    /**
     * 重置token回调
     */
    public abstract void requestAgain();
}
