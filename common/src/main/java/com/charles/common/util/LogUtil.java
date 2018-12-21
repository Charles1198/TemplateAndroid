package com.charles.common.util;

import android.util.Log;

/**
 *
 * @author charles
 * @date 2017/10/22
 */

public class LogUtil {
    /**
     * 控制是否打印log，上线时关闭
     */
    private static boolean showLog = true;

    public static void d(String logMessage) {
        if (showLog && !logMessage.isEmpty()) {
            Log.d("log", logMessage);
        }
    }

    public static void d(String tag, String logMessage) {
        if (showLog && !logMessage.isEmpty()) {
            Log.d(tag, logMessage);
        }
    }

    public static void e(String logMessage) {
        if (showLog && !logMessage.isEmpty()) {
            Log.e("log", logMessage);
        }
    }

    public static void e(String tag, String logMessage) {
        if (showLog && !logMessage.isEmpty()) {
            Log.e(tag, logMessage);
        }
    }

    public static void i(String logMessage) {
        if (showLog && !logMessage.isEmpty()) {
            Log.i("log", logMessage);
        }
    }

    public static void i(String tag, String logMessage) {
        if (showLog && !logMessage.isEmpty()) {
            Log.i(tag, logMessage);
        }
    }
}
