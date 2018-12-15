package com.charles.common.util;

import android.content.Context;
import android.content.Intent;

import com.charles.common.kv.Kv;
import com.charles.common.network.AbstractMyCallBack;
import com.charles.common.network.ApiManager;
import com.charles.common.network.NetworkUtil;
import com.charles.common.network.response.BaseResp;
import com.charles.common.network.response.TokenResp;

import retrofit2.Call;
import retrofit2.Response;

/**
 *
 * @author charles
 * @date 16/8/18
 */
public class TokenUtil {
    public static final String TOKEN = "token";
    private static final String REFRESH_TOKEN = "refresh_token";

    public static void reset(final ResetFinish finished) {
        ApiManager apiManager = NetworkUtil.createApiManager(ApiManager.class);
        final Call<BaseResp<TokenResp>> call = apiManager.resetToken(getRefreshToken());
        call.enqueue(new AbstractMyCallBack<BaseResp<TokenResp>>() {
            @Override
            public void onSuc(BaseResp<TokenResp> response) {
                saveToken(response.getData());

                finished.resetFinish(true);
            }

            @Override
            public void requestAgain() {
                finished.resetFinish(false);
            }

            @Override
            public void onFail(String statusCode) {
                finished.resetFinish(false);
            }
        });
    }

    public interface ResetFinish {

        /**
         * 重置token结果
         *
         * @param succeed 成功/失败
         */
        void resetFinish(boolean succeed);
    }

    private static void saveToken(TokenResp tokenResp) {
        String token = tokenResp.getToken();
        String refreshToken = tokenResp.getRefreshToken();
        Kv.setString(TOKEN, token);
        Kv.setString(REFRESH_TOKEN, refreshToken);
    }

    public static void clearToken() {
        Kv.remove(TOKEN);
        Kv.remove(REFRESH_TOKEN);
    }

//    private static void toLogin() {
//        removeInfo();
//        Context context = MyApplication.getContext();
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(TOKEN_EX, true);
//        context.startActivity(intent);
//    }
//
//    public static void reSaveToken(TokenResp token) {
//        SPUtils.put(TOKEN, token.getToken());
//        SPUtils.put(REFRESH_TOKEN, token.getRefresh_token());
//    }
//
//    private static void removeInfo() {
//        SPUtils.remove(Constant.TOKEN);
//        SPUtils.remove(REFRESH_TOKEN);
//        User.deleteUserInfo();
//    }
//
//    public static String getToken() {
//        return SPUtils.getString(TOKEN);
//    }
//

    private static String getRefreshToken() {
        return Kv.getString(REFRESH_TOKEN);
    }
}
