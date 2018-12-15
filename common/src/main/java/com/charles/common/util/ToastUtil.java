package com.charles.common.util;

import android.view.Gravity;

import com.hjq.toast.ToastUtils;

/**
 * @author charles
 * @date 2018/11/9
 * @description
 */
public class ToastUtil {

    public static void toast(String message) {
        ToastUtils.getToast().setGravity(Gravity.BOTTOM, 0, 400);
        ToastUtils.show(message);
    }
}
