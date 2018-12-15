package com.charles.login.function.resetpwd;

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
public class ResetPasswordPresenter {
    private ResetPasswordView resetPasswordView;
    private ApiManager apiManager;

    public ResetPasswordPresenter(ResetPasswordView resetPasswordView) {
        this.resetPasswordView = resetPasswordView;

        apiManager = NetworkUtil.createApiManager(ApiManager.class);
    }

    public void sendCode() {
        String account = resetPasswordView.getAccount();
        if (account.endsWith("@")) {
            sendCodeToEmail(account);
        } else {
            sendCodeToTel(account);
        }
    }

    private void sendCodeToEmail(String email) {
        Call<BaseResp> call = apiManager.sendCodeToEmail(email, 2);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                resetPasswordView.sendCodeToEmailSucceed();
            }

            @Override
            public void onFail(String message) {
                resetPasswordView.sendCodeFail(message);
            }

            @Override
            public void requestAgain() {

            }
        });
    }

    private void sendCodeToTel(String tel) {
        Call<BaseResp> call = apiManager.sendCodeToTel(tel, 1);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                resetPasswordView.sendCodeToTelSucceed();
            }

            @Override
            public void onFail(String message) {
                resetPasswordView.sendCodeFail(message);
            }

            @Override
            public void requestAgain() {

            }
        });
    }

    public void resetPassword() {
        String account = resetPasswordView.getAccount();
        String code = resetPasswordView.getCode();
        String password = resetPasswordView.getPassword();

        if (account.endsWith("@")) {
            resetPasswordByTel(account, code, password);
        } else {
            resetPasswordByEmail(account, code, password);
        }
    }

    private void resetPasswordByTel(String tel, String code, String password) {
        Call<BaseResp> call = apiManager.resetPasswordByTel(tel, code, password);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                resetPasswordView.resetPasswordSucceed();
            }

            @Override
            public void onFail(String message) {
                resetPasswordView.resetPasswordFail(message);
            }

            @Override
            public void requestAgain() {

            }
        });
    }

    private void resetPasswordByEmail(String email, String code, String password) {
        Call<BaseResp> call = apiManager.resetPasswordByEmail(email, code, password);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                resetPasswordView.resetPasswordSucceed();
            }

            @Override
            public void onFail(String message) {
                resetPasswordView.resetPasswordFail(message);
            }

            @Override
            public void requestAgain() {

            }
        });
    }
}
