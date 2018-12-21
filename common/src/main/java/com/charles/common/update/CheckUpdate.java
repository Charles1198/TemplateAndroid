package com.charles.common.update;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.charles.common.Constant;
import com.charles.common.base.BaseApplication;
import com.charles.common.network.AbstractMyCallBack;
import com.charles.common.network.ApiManager;
import com.charles.common.network.NetworkUtil;
import com.charles.common.network.response.BaseResp;
import com.charles.common.network.response.UpdateResp;
import com.charles.common.util.AppUtil;
import com.google.gson.Gson;

import retrofit2.Call;

/**
 * @author charles
 * @date 2018/11/15
 * @description
 */
public class CheckUpdate {
    public static String UPDATE_INFO = "updateInfo";
    public static String UPDATE_ACTION = "updateAction";
    public static String UPDATE_FORCE = "0";

    public static void checkUpdate() {
        ApiManager apiManager = NetworkUtil.createApiManager();

        String shortName = Constant.APP_NAME;
        String platform = "1";
        final String version = AppUtil.getAppVersion();

        Call<BaseResp<UpdateResp>> call = apiManager.checkUpdate(shortName, platform, version);
        call.enqueue(new AbstractMyCallBack<BaseResp<UpdateResp>>() {
            @Override
            public void onSuc(BaseResp<UpdateResp> response) {
                gotUpdateResp(response.getData());
            }

            @Override
            public void onFail(String statusCode) {
            }

            @Override
            public void requestAgain() {
            }
        });
    }

    private static void gotUpdateResp(UpdateResp updateResp) {
        String curVersion = AppUtil.getAppVersion();
        String version = updateResp.getVersion();
        if (curVersion.compareTo(version) == -1) {
            // 有更新，发送更新广播
            Gson gson = new Gson();
            String updateString = gson.toJson(updateResp);
            Intent updateIntent = new Intent(UPDATE_ACTION);
            updateIntent.putExtra(UPDATE_INFO, updateString);
            BaseApplication.getContext().sendBroadcast(updateIntent);
        }
    }

    public interface CheckUpdateListener {
        /**
         * 检查更新的结果
         *
         * @param updateResp 更新信息：null表示没有更新，当前已经是最新版本
         */
        void checkUpdateSucceed(@Nullable UpdateResp updateResp);
    }
}
