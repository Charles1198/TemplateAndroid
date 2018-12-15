package com.charles.common.error.model;

import android.support.annotation.Nullable;

import com.charles.common.network.response.BaseResp;
import com.charles.common.util.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author charles
 * @date 2018/10/31
 * @description
 */
public class ApiErrorInfo {

    /**
     * requestURL : http://api-test.bqteam.com/api/v1/test
     * requestHeader : Content-Type=application/json
     * requestMethod : POST
     * requestParams : k1=v1&k2=v2
     * responseStatusCode : S1020201
     * responseBody : {"statusCode":"S1020201","data":[],"msg":"success"}
     * responseAt : 1974-08-11 23:31:20
     */

    private String requestURL;
    private String requestHeader;
    private String requestMethod;
    private String requestParams;
    private String responseHTTPStatusCode;
    private String responseStatusCode;
    private String responseBody;
    private String responseAt;

    public ApiErrorInfo(Call call, @Nullable Response response) {
        Request request = call.request();
        requestURL = request.url().toString();
        requestMethod = request.method();
        requestParams = request.body() != null ? request.body().toString() : "";
        responseAt = DateUtil.getCurTime();

        if (request.body() != null) {
            requestParams = paramsToJson(request.body());
        } else {
            requestParams = "";
        }

        if (response != null) {
            requestHeader = headersToJson(response.raw().networkResponse().request().headers());
            BaseResp baseResp = (BaseResp) response.body();
            responseHTTPStatusCode = String.valueOf(response.code());
            if (response.body() != null) {
                responseStatusCode = baseResp.getStatusCode();
                responseBody = baseResp.toString();
            } else {
                responseStatusCode = responseHTTPStatusCode;
                responseBody = String.format("{\"statusCode\":\"%s\",\"data\":[],\"msg\":\"%s 非正常响应\"}",
                        responseHTTPStatusCode, responseHTTPStatusCode);
            }
        } else {
            requestHeader = "";
            responseHTTPStatusCode = "500";
            responseStatusCode = responseHTTPStatusCode;
            responseBody = "{\"statusCode\":\"500\",\"data\":[],\"msg\":\"500 非正常响应\"}";
        }
    }

    /**
     * 构造json形式参数
     *
     * @param requestBody
     * @return
     */
    private static String paramsToJson(RequestBody requestBody) {
        // TODO: 2018/11/20 现在还没有 FormBody 形式的 post 请求，等有了之后再测试下是不是起作用
//        if ((requestBody instanceof FormBody)) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
//                    jsonObject.putOpt(((FormBody) requestBody).encodedName(i), ((FormBody) requestBody).encodedValue(i));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return  jsonObject.toString();
//        }

        String paramsStr = "";
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
             paramsStr = buffer.readString(charset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return paramsStr;
    }

    /**
     * 构造json形式请求头
     *
     * @param headers
     * @return
     */
    private String headersToJson(Headers headers) {
        Set<String> headerNames = headers.names();

        JSONObject jsonObject = new JSONObject();
        try {
            for (String name : headerNames) {
                jsonObject.put(name, headers.get(name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("requestURL", requestURL);
            jsonObject.putOpt("requestHeader", requestHeader);
            jsonObject.putOpt("requestMethod", requestMethod);
            jsonObject.putOpt("requestParams", requestParams);
            jsonObject.putOpt("responseHTTPStatusCode", responseHTTPStatusCode);
            jsonObject.putOpt("responseStatusCode", responseStatusCode);
            jsonObject.putOpt("responseBody", responseBody);
            jsonObject.putOpt("responseAt", responseAt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
