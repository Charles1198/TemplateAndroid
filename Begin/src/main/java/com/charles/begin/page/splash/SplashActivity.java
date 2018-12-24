package com.charles.begin.page.splash;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.alibaba.android.arouter.launcher.ARouter;
import com.charles.common.base.BaseActivity;
import com.charles.common.network.response.ServerResp;
import com.charles.common.util.ToastUtil;
import com.charles.begin.R;
import com.charles.begin.page.guide.GuideActivity;

import java.util.List;

/**
 * @author charles
 */
public class SplashActivity extends BaseActivity implements SplashView {
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            ARouter.getInstance().build("/login/LoginActivity").navigation();
            finish();
        }, 1000);

//        splashPresenter = new SplashPresenter(this);
//        splashPresenter.start();
    }

    @Override
    public void toLogin() {
        ARouter.getInstance().build("/login/LoginActivity").navigation();
    }

    @Override
    public void toMain() {
        ARouter.getInstance().build("/app/MainTabActivity").navigation();
    }

    @Override
    public void toGuide() {
        startActivity(new Intent(this, GuideActivity.class));
    }

    @Override
    public void noBaseUrl() {
        AlertDialog.Builder noBaseUrlDialog = new AlertDialog.Builder(this);
        noBaseUrlDialog.setTitle("无法连接网络，请检查您的网络状态")
                .setPositiveButton("好的", (dialog, which) -> watchNetworkState())
                .show();
    }

    private void watchNetworkState() {
        // TODO: 2018/11/23 api小于21的另做处理
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.requestNetwork(new NetworkRequest.Builder().build(),
                    new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            super.onAvailable(network);

                            // 已联网，重新start
                            splashPresenter.start();
                            ToastUtil.toast("已联网");
                        }
                    });
        }
    }

    @Override
    public void noServer() {
        AlertDialog.Builder noServerDialog = new AlertDialog.Builder(this);
        noServerDialog.setTitle("很抱歉，没有找到服务器")
                .setPositiveButton("重试", (dialog, which) -> splashPresenter.getServerList())
                .setNegativeButton("反馈", (dialog, which) ->
                        ARouter.getInstance().build("/user/FeedbackActivity").navigation())
                .setNeutralButton("退出", (dialog, which) -> finish())
                .show();
    }

    @Override
    public void multiServer(final List<ServerResp> serverList) {
        CharSequence[] serverNames = new CharSequence[serverList.size()];
        for (int i = 0; i < serverList.size(); i++) {
            serverNames[i] = serverList.get(i).getServerName();
        }

        AlertDialog.Builder serverListDialog = new AlertDialog.Builder(this);
        serverListDialog.setTitle("请选择服务器");
        serverListDialog.setItems(serverNames, (dialog, which) ->
                splashPresenter.checkServerUrl(serverList.get(which).getServerURL())).show();
    }
}
