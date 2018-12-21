package com.charles.common.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.charles.common.error.CrashHandler;
import com.charles.common.util.ToastUtil;
import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;



/**
 * @author charles
 * @date 2018/10/8
 * @description
 */
public class BaseApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static Context getContext() {
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        // 初始化MMKV
        MMKV.initialize(this);

        ToastUtils.init(this);

        //捕获程序异常崩溃的方法，上线时打开
//        new CrashHandler();
    }
}
