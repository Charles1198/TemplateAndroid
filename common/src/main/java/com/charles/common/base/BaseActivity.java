package com.charles.common.base;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.charles.common.BuildConfig;
import com.charles.common.R;
import com.charles.common.network.response.UpdateResp;
import com.charles.common.update.CheckUpdate;
import com.charles.common.update.UpdateService;
import com.charles.common.util.FileUtil;
import com.charles.common.util.ToastUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.Objects;

/**
 * @author charles
 * @date 2017/2/18
 */

public class BaseActivity extends AppCompatActivity implements BaseView {
    public static String TAG;

    /**
     * 标志当前 activity 是否存在，防止 dialog 显示或隐藏的时候出现崩溃
     */
    public boolean isRunning = false;

    private AlertDialog loadingAlert;

    private UpdateBroadcast updateBroadcast;
    private InstallReceiver installReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getComponentName().getShortClassName();

        updateBroadcast = new UpdateBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CheckUpdate.UPDATE_ACTION);
        registerReceiver(updateBroadcast, intentFilter);

        installReceiver = new InstallReceiver();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(installReceiver, intentFilter2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(updateBroadcast);
        unregisterReceiver(installReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        hideLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void noNetwork() {

    }

    @Override
    public void showLoading(String message) {
        if (isRunning && loadingAlert == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            View loadingView = LayoutInflater.from(this).inflate(R.layout.view_dialog_loading, null);
            if (message != null) {
                TextView loadingTitle = loadingView.findViewById(R.id.loading_title);
                loadingTitle.setText(message);
            }
            builder.setView(loadingView);
            loadingAlert = builder.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingAlert != null) {
            loadingAlert.dismiss();
            loadingAlert = null;
        }
    }

    public void hideKeyboard() {
        //隐藏键盘
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void gotUpdateInfo(UpdateResp updateResp) {
        String features = "";
        for (String feature : updateResp.getDesc().getFeatures()) {
            features += "    " + feature + "；\n";
        }
        String bugs = "";
        for (String bug : updateResp.getDesc().getBugs()) {
            bugs += "    " + bug + "；\n";
        }
        String appVersionDesc = String.format("新特性：\n%s修复：\n%s", features, bugs);

        TextView textView = new TextView(this);
        textView.setText(appVersionDesc);
        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 80;
        lp.rightMargin = 80;
        textView.setLayoutParams(lp);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(textView);

        boolean isForceUpdate = CheckUpdate.UPDATE_FORCE.equals(updateResp.getForceUpdate());

        AlertDialog.Builder updateAlert = new AlertDialog.Builder(this);
        updateAlert.setCancelable(false);
        updateAlert.setTitle("检查到版本更新");
        updateAlert.setView(frameLayout);
        updateAlert.setPositiveButton("立即更新", (dialog, which) -> {
            downloadApk();
            if (isForceUpdate) {
                showUpdateProgress();
            }
        });
        if (isForceUpdate) {
            // 强制更新
            updateAlert.setNegativeButton("退出", (dialog, which) -> exitApp());
        } else {
            updateAlert.setNegativeButton("暂不更新", null);
        }

        updateAlert.show();
    }

    private void downloadApk() {
        Intent intent = new Intent(this, UpdateService.class);
        startService(intent);
    }

    private void showUpdateProgress() {
    }

    private void exitApp() {

    }

    /**
     * 安装app
     */
    private void installApk() {
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "teacher.apk");
        if (!file.exists()) {
            ToastUtil.toast("apk不存在");
            return;
        }

        //下载完成之后安装APP
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri = FileProvider.getUriForFile(this, "com.charles.android.fileprovider", file);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 接受更新广播
     */
    public class UpdateBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String updateString = intent.getStringExtra(CheckUpdate.UPDATE_INFO);
            Gson gson = new Gson();
            UpdateResp updateResp = gson.fromJson(updateString, UpdateResp.class);
            gotUpdateInfo(updateResp);
        }
    }

    public class InstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            installApk();
        }
    }
}
