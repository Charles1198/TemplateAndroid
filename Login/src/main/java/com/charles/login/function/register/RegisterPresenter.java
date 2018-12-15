package com.charles.login.function.register;

import com.charles.common.network.AbstractMyCallBack;
import com.charles.common.network.NetworkUtil;
import com.charles.common.network.response.BaseResp;
import com.charles.login.api.ApiManager;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author charles
 * @date 2018/10/9
 * @description
 */
class RegisterPresenter {
    private RegisterView registerView;
    private ApiManager apiManager;

    RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;

        apiManager = NetworkUtil.createApiManager(ApiManager.class);
    }

    void sendCode() {
        String account = registerView.getAccount();
        if (account.contains("@")) {
            sendCodeToEmail(account);
        } else {
            sendCodeToTel(account);
        }
    }

    private void sendCodeToEmail(String email) {
        registerView.showLoading("正在获取验证码");

        Call<BaseResp> call = apiManager.sendCodeToEmail(email, 2);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                registerView.hideLoading();
                registerView.sendCodeToEmailSucceed();
            }

            @Override
            public void onFail(String message) {
                registerView.hideLoading();
                registerView.sendCodeFail(message);
            }

            @Override
            public void requestAgain() {

            }
        });
    }

    private void sendCodeToTel(String tel) {
        registerView.showLoading("正在获取验证码");

        Call<BaseResp> call = apiManager.sendCodeToTel(tel, 1);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                registerView.hideLoading();
                registerView.sendCodeToTelSucceed();
            }

            @Override
            public void onFail(String message) {
                registerView.hideLoading();
                registerView.sendCodeFail(message);
            }

            @Override
            public void requestAgain() {

            }
        });
    }

    void register() {
        String account = registerView.getAccount();
        String code = registerView.getCode();
        String password = registerView.getPassword();

        if (account.contains("@")) {
            registerByEmail(account, password, code);
        } else {
            registerByTel(account, password, code);
        }
    }

    private void registerByTel(String tel, String password, String code) {
        registerView.showLoading("正在注册");

        Call<BaseResp> call = apiManager.registerByTel(tel, password, code);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                registerView.hideLoading();
                registerView.registerSucceed();
            }

            @Override
            public void onFail(String message) {
                registerView.hideLoading();
                registerView.registerFail(message);
            }

            @Override
            public void requestAgain() {

            }
        });
    }

    private void registerByEmail(String email, String password, String code) {
        registerView.showLoading("正在注册");

        Call<BaseResp> call = apiManager.registerByEmail(email, password, code);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                registerView.hideLoading();
                registerView.registerSucceed();
            }

            @Override
            public void onFail(String message) {
                registerView.hideLoading();
                registerView.registerFail(message);
            }

            @Override
            public void requestAgain() {

            }
        });
    }
}
