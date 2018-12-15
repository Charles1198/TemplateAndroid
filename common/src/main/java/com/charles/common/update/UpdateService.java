package com.charles.common.update;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.charles.common.Constant;
import com.charles.common.app.BaseApplication;
import com.charles.common.network.ApiManager;
import com.charles.common.network.NetworkUtil;
import com.charles.common.util.FileUtil;
import com.charles.common.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 *
 * @author charles
 * @date 16/11/26
 */

public class UpdateService extends Service {
    public static String UPDATE_URL = "updateUrl";
    public static String INSTALL_ACTION = "installAction";

    private Retrofit.Builder retrofit;
    public static final boolean forceUpdate = false;
    private int progress = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String downloadUrl = "http://database-of-charles.oss-cn-qingdao.aliyuncs.com/video/video.mp4";
//        String downloadUrl = Objects.requireNonNull(intent.getExtras()).getString(UPDATE_URL);
        downloadApk();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void downloadApk() {
        String downloadUrl = "http://database-of-charles.oss-cn-qingdao.aliyuncs.com/apk/app-api-tomcat-test.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle("易教考");
        request.setDescription("下载新版本");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(BaseApplication.getContext(), Environment.DIRECTORY_DOWNLOADS, "teacher.apk");

        DownloadManager manager = (DownloadManager) BaseApplication.getContext().getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
