package com.example.charles.templateandroid.page.splash;

import com.charles.common.base.BaseView;
import com.charles.common.network.response.ServerResp;

import java.util.List;

/**
 * @author charles
 * @date 2018/10/10
 * @description
 */
public interface SplashView extends BaseView {
    /**
     * 进入登陆页面
     */
    void toLogin();

    /**
     * 进入主页面
     */
    void toMain();

    /**
     * 进入引导页
     */
    void toGuide();

    /**
     * 没联网，也没有baseUrl
     */
    void noBaseUrl();

    /**
     * 没有获取到服务器，可能是网络原因或程序bug，通知用户检查网络或提交反馈
     */
    void noServer();

    /**
     * 获取到多个服务器，需要用户手动选择服务器
     *
     * @param serverList
     */
    void multiServer(List<ServerResp> serverList);
}
