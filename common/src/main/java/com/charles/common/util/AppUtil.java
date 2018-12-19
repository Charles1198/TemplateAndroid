package com.charles.common.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.charles.common.Constant;
import com.charles.common.app.BaseApplication;
import com.charles.common.kv.Kv;

/**
 * @author charles
 * @date 15/10/26
 */
public class AppUtil {
    /**
     * @return 返回当前版本号
     */
    public static String getAppVersion() {
        Context context = BaseApplication.getContext();
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 获取渠道名称
     * <p>
     * 需要正确设置渠道
     *
     * @return
     */
    public static String getChannelName() {
        String channelName = "";
        try {
            Context context = BaseApplication.getContext();
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            channelName = applicationInfo.metaData.getString(Constant.CHANNEL_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 延时函数
     */
    private void delay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // do something
            }
        }, 10);
    }
}
