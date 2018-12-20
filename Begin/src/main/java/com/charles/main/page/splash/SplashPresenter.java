package com.charles.main.page.splash;

import com.charles.common.Constant;
import com.charles.common.device.DeviceInfo;
import com.charles.common.device.GetDeviceInfo;
import com.charles.common.kv.Kv;
import com.charles.common.network.AbstractMyCallBack;
import com.charles.common.network.ApiManager;
import com.charles.common.network.NetworkUtil;
import com.charles.common.network.SubmitError;
import com.charles.common.network.response.BaseResp;
import com.charles.common.network.response.ServerResp;
import com.charles.common.network.response.TokenResp;
import com.charles.common.network.response.UUIDResp;
import com.charles.common.update.CheckUpdate;
import com.charles.common.util.AppUtil;
import com.charles.main.page.guide.GuideActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author charles
 * @date 2018/10/10
 * @description
 */
public class SplashPresenter {
    private SplashView splashView;
    private ApiManager apiManager;

    SplashPresenter(SplashView splashView) {
        this.splashView = splashView;
        apiManager = NetworkUtil.createApiManager();
    }

    public void start() {
        // 检查网络连接状态
        if (!NetworkUtil.isNetworkConnected()) {
            // 网络未连接，检查是否存在baseUrl
            if (Kv.contains(Constant.BASE_URL)) {
                // 存在baseUrl，检查是否存在token
                if (Kv.contains(Constant.TOKEN)) {
                    // token存在，跳转到主页面
                    splashView.toMain();
                } else {
                    // token不存在，跳转到登录页面
                    splashView.toLogin();
                }
            } else {
                // 不存在baseUrl，弹出对话框提醒用户联网；同时监听网络连接状态
                splashView.noBaseUrl();
            }
        } else {
            // 检查是否存在UUID
            if (!Kv.contains(Constant.UUID)) {
                // 不存在uuid，去获取uuid
                getUUID();
            } else {
                // 存在uuid，检查是否存在激活信息
                checkRegisterInfo();
            }

            // 检查版本更新。没有更新不做操作，有更新发出广播，在BaseActivity注册广播接收器，收到更新广播弹出更新对话框
            CheckUpdate.checkUpdate();

            // 检查是否有崩溃信息，有的话上传
            if (Kv.contains(Constant.FILE_CRASH)) {
                SubmitError.sendException();
            }
        }
    }

    private void checkRegisterInfo() {
        // 检查是否存在激活信息
        if (!Kv.contains(Constant.REGISTER_APP)) {
            // 不存在激活信息，去激活应用
            registerApp();
        } else {
            // 存在激活信息，检查激活信息是否过期（应用更新等）
            String registerInfo = Kv.getString(Constant.REGISTER_APP);
            if (!registerInfo.equals(registerInfo())) {
                // 激活信息过期，重新激活应用
                registerApp();
            } else {
                // 激活信息未过期，检查是否存在baseUrl
                if (Kv.contains(Constant.BASE_URL)) {
                    // baseUrl存在，检查是否可用
                    checkBaseUrl();
                } else {
                    // baseUrl不存在，获取server列表
                    getServerList();
                }
            }
        }
    }

    /**
     * 检查baseUrl
     */
    private void checkBaseUrl() {
        if (Kv.contains(Constant.BASE_URL)) {
            // baseUrl存在，检查是否可用
            baseUrlConnectivity();
        } else {
            // baseUrl不存在，获取server列表
            getServerList();
        }
    }

    /**
     * 检查baseUrl是否可用
     */
    private void baseUrlConnectivity() {
        String baseUrl = Kv.getString(Constant.BASE_URL);
        ApiManager apiManager = new Retrofit.Builder().baseUrl(baseUrl).build().create(ApiManager.class);
        Call<Void> call = apiManager.connectServer();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // baseUrl可用，检查是否首次登陆
                if (Kv.contains(GuideActivity.GUIDE_SKIM)) {
                    // 非首次登陆，检查是否存在token
                    checkToken();
                } else {
                    // 首次登陆，进入引导页
                    splashView.toGuide();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // baseUrl不可用，获取server列表
                getServerList();
            }
        });
    }

    /**
     * 检查token存在性及是否过期
     */
    private void checkToken() {
        // 检查token是否存在
        if (!Kv.contains(Constant.TOKEN)) {
            // token不存在，进入登录页
            splashView.toLogin();
            return;
        }

        // token存在，检查是否过期
        String token = Kv.getString(Constant.TOKEN);
        ApiManager apiManager = NetworkUtil.createApiManager(ApiManager.class);
        Call<BaseResp<TokenResp>> call = apiManager.checkToken(token);
        call.enqueue(new AbstractMyCallBack<BaseResp<TokenResp>>() {
            @Override
            public void onSuc(BaseResp<TokenResp> response) {
                // token过期
                splashView.toLogin();
            }

            @Override
            public void onFail(String statusCode) {
                // token未过期
                splashView.toMain();
            }

            @Override
            public void requestAgain() {
                // 使用refreshToken更新了token
                splashView.toMain();
            }
        });
    }

    /**
     * 获取uuid
     */
    private void getUUID() {
        DeviceInfo deviceInfo = GetDeviceInfo.getDeviceInfo();
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                deviceInfo.toJsonString());

        Call<BaseResp<UUIDResp>> call = apiManager.getUUID(requestBody);
        call.enqueue(new AbstractMyCallBack<BaseResp<UUIDResp>>() {
            @Override
            public void onSuc(BaseResp<UUIDResp> response) {
                if (!response.getData().getUuid().isEmpty()) {
                    // 获取到uuid，保存下来
                    Kv.setString(Constant.UUID, response.getData().getUuid());
                    // 检查注册信息
                    checkRegisterInfo();
                } else {
                    // 没获取到uuid，那肯定没法注册，跳过，检查是否存在baseUrl
                    checkBaseUrl();
                }
            }

            @Override
            public void onFail(String statusCode) {
                checkToken();
            }

            @Override
            public void requestAgain() {
                getUUID();
            }
        });
    }

    /**
     * 注册应用
     */
    private void registerApp() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"), registerInfo());
        Call<BaseResp> call = apiManager.registerApp(requestBody);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                // 注册成功，保存注册信息d
                Kv.setString(Constant.REGISTER_APP, registerInfo());

                if (Kv.contains(Constant.TOKEN)) {
                    checkToken();
                } else {
                    splashView.toLogin();
                }
            }

            @Override
            public void onFail(String statusCode) {
                if (Kv.contains(Constant.TOKEN)) {
                    checkToken();
                } else {
                    splashView.toLogin();
                }
            }

            @Override
            public void requestAgain() {

            }
        });
    }

    private String registerInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("shortName", Constant.APP_NAME);
            jsonObject.putOpt("installChannel", AppUtil.getChannelName());
            jsonObject.putOpt("appVersion", AppUtil.getAppVersion());
            jsonObject.putOpt("deviceUUID", Kv.getString(Constant.UUID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 获取应用服务器选择连通性最好的服务器地址；如果有多个服务器需要用户选择使用哪个
     */
    void getServerList() {
        Call<BaseResp<List<ServerResp>>> call = apiManager.checkUrl(Constant.APP_NAME);
        call.enqueue(new AbstractMyCallBack<BaseResp<List<ServerResp>>>() {
            @Override
            public void onSuc(BaseResp<List<ServerResp>> response) {
                if (response.getData() == null) {
                    // 没有获取到服务器
                    splashView.noServer();
                } else if (response.getData().size() == 0) {
                    // 没有获取到服务器
                    splashView.noServer();
                } else if (response.getData().size() == 1) {
                    // 服务器列表只有一个，那么自动选择连通性最好的线路
                    checkServerUrl(response.getData().get(0).getServerURL());
                } else {
                    // 服务器列表有多个，需要用户来选择他要连哪个服务器
                    splashView.multiServer(response.getData());
                }
            }

            @Override
            public void onFail(String statusCode) {
                splashView.noServer();
            }

            @Override
            public void requestAgain() {

            }
        });
    }

    /**
     * 检测连接速度最快的地址，并保存为baseUrl
     *
     * @param urlList 地址列表
     */
    void checkServerUrl(List<String> urlList) {
        // TODO: 2018/11/25 因为serverList还是假数据，所以跳过
        // 检查是否首次登陆
        if (Kv.contains(GuideActivity.GUIDE_SKIM)) {
            // 非首次登陆，检查是否存在token
            checkToken();
        } else {
            // 首次登陆，进入引导页
            splashView.toGuide();
        }
//        NetworkUtil.fastestServer(urlList, url -> {
//            if (url.isEmpty()) {
//                // 所有url都无法连通，弹框提示用户重试或反馈
//                splashView.noServer();
//            } else {
//                // 保存baseUrl
//                Kv.setString(Constant.BASE_URL, url);
//                // 检查是否首次登陆
//                if (Kv.contains(GuideActivity.GUIDE_SKIM)) {
//                    // 非首次登陆，检查是否存在token
//                    checkToken();
//                } else {
//                    // 首次登陆，进入引导页
//                    splashView.toGuide();
//                }
//            }
//        });
    }

//    public void checkUrlList() {
//        List<String> urlList = new ArrayList<>();
//        urlList.add("https://www.baidu.com");
//        urlList.add("https://www.zhihu.com");
//        urlList.add("http://jiayueji.cn");
//        urlList.add("https://www.jianshu.com");
//        NetworkUtil.fastestServer(urlList, new NetworkUtil.FastestServerListener() {
//            @Override
//            public void fastestServer(String url) {
//                LogUtil.d("fastestServer is", url);
//            }
//        });
//    }
}
