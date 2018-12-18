package com.charles.login.page.login;

import android.os.Handler;

import com.charles.common.kv.Kv;
import com.charles.common.update.CheckUpdate;
import com.charles.login.LoginConst;
import com.charles.login.api.ApiManager;
import com.charles.common.network.AbstractMyCallBack;
import com.charles.common.network.NetworkUtil;
import com.charles.common.network.response.BaseResp;
import com.charles.common.network.response.TokenResp;

import retrofit2.Call;

/**
 * @author charles
 * @date 2018/10/5
 * @description
 */
public class LoginPresenter {
    public static final String TEST_USERNAME = "username";
    public static final String TEST_PASSWORD = "password";
    private LoginView loginView;
    private ApiManager apiManager;

    LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        apiManager = NetworkUtil.createApiManager(ApiManager.class);
    }

    public void login() {
        if (!NetworkUtil.isNetworkConnected()) {
            loginView.noNetwork();
            return;
        }

        loginView.showLoading("正在登陆");

        final String account = loginView.getAccount();
        final String password = loginView.getPassword();

        // 模拟网络连接
        new Handler().postDelayed(() -> {
            loginView.hideLoading();
            if (account.equals(TEST_USERNAME) && password.equals(TEST_PASSWORD)) {
                loginView.loginSucceed();
            } else {
                loginView.loginFail("用户名或密码错误(username/password)");
            }
        }, 3000);

        /*
        Call<BaseResp<TokenResp>> call = apiManager.login(account, password);
        call.enqueue(new AbstractMyCallBack<BaseResp<TokenResp>>() {
            @Override
            public void onSuc(BaseResp<TokenResp> response) {
                loginView.loginSucceed();

                saveAccount(account, password);
            }

            @Override
            public void onFail(String message) {
                loginView.loginFail(message);
            }

            @Override
            public void requestAgain() {
            }
        });
        */
    }

    /*
    private void saveAccount(String account, String password) {
        if (Kv.getBool(LoginConst.REMEMBER_ACCOUNT, true)) {
            Kv.setString(LoginConst.ACCOUNT, account);
            Kv.setString(LoginConst.PASSWORD, password);
        }
    }
    */
}
