package com.charles.common.error;

import android.os.Looper;
import android.widget.Toast;

import com.charles.common.Constant;
import com.charles.common.app.BaseApplication;
import com.charles.common.error.model.ExceptionInfo;
import com.charles.common.kv.Kv;

/**
 *
 * @author charles
 * @date 16/3/24
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler exceptionHandler;

    public CrashHandler() {
        exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当未捕获的异常发生时会交由该函数处理
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        //自己没有处理该异常的话，则交给系统默认的异常处理器来处理
        if (!handleUncaughtException(thread, ex) && exceptionHandler != null) {
            exceptionHandler.uncaughtException(thread, ex);
        } else {
            // 等待1s，确保崩溃信息存储完毕
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.exit(0);
        }
    }

    /**
     * 自己处理异常
     *
     * @param ex 异常信息
     * @return true：程序自己处理了异常
     */
    private boolean handleUncaughtException(final Thread thread, final Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 创建并保存崩溃信息
        ExceptionInfo exceptionInfo = new ExceptionInfo(thread, ex);
        Kv.setString(Constant.FILE_CRASH, exceptionInfo.toJsonString());

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(BaseApplication.getContext(), "很抱歉,程序出现异常.我们会第一时间修复,请您耐心等候.",
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}
