package com.example.charles.templateandroid.application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.charles.common.app.BaseApplication;

/**
 *
 * @author charles
 * @date 15/11/19
 * 全局获取context
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // 打印日志
        ARouter.openLog();
        // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.openDebug();
        ARouter.init(this);
    }
}
