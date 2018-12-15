package com.charles.login.function.login;

import android.support.annotation.Nullable;

import com.charles.common.kv.Kv;
import com.charles.common.network.response.UpdateResp;
import com.charles.common.update.CheckUpdate;
import com.charles.login.LoginConst;
import com.charles.login.api.ApiManager;
import com.charles.common.network.AbstractMyCallBack;
import com.charles.common.network.NetworkUtil;
import com.charles.common.network.response.BaseResp;
import com.charles.common.network.response.TokenResp;
import com.charles.common.util.StringUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author charles
 * @date 2018/10/5
 * @description
 */
public class LoginPresenter {
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

        final String account = loginView.getAccount();
        final String password = loginView.getPassword();

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
    }

    private void saveAccount(String account, String password) {
        if (Kv.getBool(LoginConst.REMEMBER_ACCOUNT, true)) {
            Kv.setString(LoginConst.ACCOUNT, account);
            Kv.setString(LoginConst.PASSWORD, password);
        }
    }

    private void checkUpdate() {
        CheckUpdate.checkUpdate();
    }
}
