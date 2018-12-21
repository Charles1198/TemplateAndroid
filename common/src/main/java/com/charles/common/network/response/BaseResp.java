package com.charles.common.network.response;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * @author charles
 * @date 16/9/29
 * <p>
 * 返回数据的基本格式
 */

public class BaseResp<T> {

    /**
     * 响应数据，
     */
    private T data;
    /**
     * 响应码
     */
    private String statusCode;
    /**
     * 响应信息
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


    @NonNull
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
