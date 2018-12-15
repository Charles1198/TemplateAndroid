package com.charles.common.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.charles.common.Constant;
import com.charles.common.app.BaseApplication;
import com.charles.common.util.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author charles
 * @date 16/9/29
 */

public class NetworkUtil {
    public static final String SUCCESS = "S0000000";
    /**
     * AppManager地址
     */
    private static final String APP_MANAGER_ADDRESS = "https://yapi.bqteam.com/mock/11/";
    /**
     * 应用服务器地址
     */
//    private static final String BASE_URL = "http://container-tomcat-test-8-jre8.bqteam.com/jszgz/";
    private static final String BASE_URL = "https://api-tomcat-test.bqteam.com/jszgz/";

    /**
     * 三种网络情况
     */
    private static final int NO_NETWORK = 0;
    private static final int WIFI = 2;
    private static final int MOBILE = 1;
    private static final String WIFI_STRING = "wifi";
    private static final String MOBILE_STRING = "mobile";
    private static final String NO_NETWORK_STRING = "noNetwork";

    @SuppressLint("DefaultLocale")
    private static final String USER_AGENT = String.format("ShortName/%s AppVersion/%s OSRelease/Android@%d Model/%s Brand/%s", Constant.APP_NAME, Tools.getAppVersion(), Build.VERSION.SDK_INT, Build.MODEL, Build.BRAND);

    /**
     * 从服务器返回回来的出错信息
     */
    private static String timeoutString = "java.net.SocketTimeoutException";
    private static String jsonSyntaxString = "java.lang.IllegalStateException";
    private static String connectException = "java.net.ConnectException";
    private static String unknownHostException = "java.net.UnknownHostException";

    /**
     * 创建接口管理类，管理 appManager 的接口
     *
     * @return
     */
    public static <T> T createApiManager(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(setupClient())
                .build();
        return retrofit.create(clazz);
    }

    /**
     * 请求头
     *
     * @return
     */
    public static OkHttpClient setupClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(5000, TimeUnit.MILLISECONDS);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("User-Agent", USER_AGENT)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });
        return httpClient.build();
    }

    /**
     * 创建接口管理类，管理 appManager 的接口
     *
     * @return
     */
    public static ApiManager createApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APP_MANAGER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(setupClient())
                .build();
        return retrofit.create(ApiManager.class);
    }

    /**
     * 检查网络是否连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager manager =
                (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 检查网络状态
     *
     * @return 网络的类型和状态
     */
    public static int checkNetwork() {
        ConnectivityManager manager =
                (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return NO_NETWORK;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return WIFI;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return MOBILE;
        }
        return NO_NETWORK;
    }

    /**
     * 获取网络链接类型
     *
     * @return
     */
    public static String getNetworkState() {
        ConnectivityManager manager =
                (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NO_NETWORK_STRING;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return WIFI_STRING;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return MOBILE_STRING;
        }
        return NO_NETWORK_STRING;
    }

    /**
     * 检测并返回连通性最好的url
     *
     * @param urlList
     */
    public static void fastestServer(List<String> urlList, final FastestServerListener listener) {
        if (urlList.size() == 1) {
            // 只有一条url，那还选啥
            listener.fastestServer(urlList.get(0));
            return;
        }

        // 构造各个url请求
        final List<Call<Void>> callList = new ArrayList<>();
        for (String url : urlList) {
            ApiManager apiManager = new Retrofit.Builder().baseUrl(url).build().create(ApiManager.class);
            Call<Void> call = apiManager.connectServer();
            callList.add(call);
        }

        // 同时异步执行各个url请求，最先得到响应的请求url就是响应最快的url。得到响应后取消其他请求
        for (Call<Void> call : callList) {
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    // 第一个请求到的就是最快的url
                    String url = call.request().url().toString();
                    listener.fastestServer(url);

                    // 取消其他请求
                    cancelCalls(callList);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // 将该call从callList中删掉
                    // 当callList被删光，说明所有call都失败了，那么url参数传""表示所有url都无法连通
                    callList.remove(call);
                    if (callList.size() == 0) {
                        listener.fastestServer("");
                    }
                }
            });
        }
    }

    /**
     * 取消请求
     *
     * @param callList 请求列表
     */
    private static void cancelCalls(List<Call<Void>> callList) {
        for (Call<Void> call : callList) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
    }

    /**
     * 监听并返回连接速度最快的url
     */
    public interface FastestServerListener {
        /**
         * 返回连接速度最快的url
         *
         * @param url
         */
        void fastestServer(String url);
    }
}
