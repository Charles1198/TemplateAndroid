package com.charles.common.update;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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

import static android.content.ContentValues.TAG;

/**
 *
 * @author charles
 * @date 16/11/26
 */

public class UpdateService extends Service {
    public static String UPDATE_URL = "updateUrl";
    public static String UPDATE_DOWNLOADED_AOK = "updateDownloadedApk";

    private Context context;

    private BroadcastReceiver broadcastReceiver;

    private DownloadManager downloadManager;
    private long downloadId;


    private OnUpdateListener mUpdateListener;

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
        context = BaseApplication.getContext();
        /**
         * 得到需要更新的安装包的下载地址
         */
        String downloadUrl = intent.getExtras().getString(UPDATE_URL);
        downloadApk(downloadUrl);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void downloadApk(String downloadUrl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle("易教考");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,
                Constant.APP_NAME + ".apk");

        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);
        listener();
    }

    private void listener() {
        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (ID == downloadId) {
                    Uri downloadUri = downloadManager.getUriForDownloadedFile(downloadId);
                    Log.d(TAG, "downloadApk: " + downloadUri);
                    installApk();
                }
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void installApk() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            // 6.0及一下
            installApkInAndroid6();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 7.0
            installApkInAndroid7();
        } else {
            // 8.0及以上
            installApkInAndroid8();
        }

        //停止service
        stopSelf();
    }

    private void installApkInAndroid6() {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 6.0以下
            Uri downloadUri = downloadManager.getUriForDownloadedFile(downloadId);
            installIntent.setDataAndType(downloadUri, "application/vnd.android.package-archive");
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 6.0
            File apkFile = queryDownloadedApk(context, downloadId);
            installIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(installIntent);
    }

    private void installApkInAndroid7() {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(context, "com.bqteam.math.android_gaoshu_client.fileprovider",
                new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), Constant.APP_NAME + ".apk"));
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(installIntent);
    }

    private void installApkInAndroid8() {
        //先获取是否有安装未知来源应用的权限
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                // 没有权限，弹窗，并去设置页面授权
                final AndroidOInstallPermissionListener listener = new AndroidOInstallPermissionListener() {
                    @Override
                    public void permissionSuccess() {
                        installApkInAndroid7();
                    }

                    @Override
                    public void permissionFail() {
                        Toast.makeText(context, "授权失败，无法安装应用", Toast.LENGTH_SHORT).show();
                    }
                };

                AndroidOPermissionActivity.sListener = listener;
                Intent intent1 = new Intent(context, AndroidOPermissionActivity.class);
                context.startActivity(intent1);
            } else {
                installApkInAndroid7();
            }
        } else {
            installApkInAndroid7();
        }
    }

    /**
     * android6.0 中获取apk安装目录的方法
     *
     * @param context
     * @param downloadId
     * @return
     */
    public static File queryDownloadedApk(Context context, long downloadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    public interface OnUpdateListener {
        void update(int currentByte, int totalByte);
    }

    public interface AndroidOInstallPermissionListener {
        void permissionSuccess();

        void permissionFail();
    }
}