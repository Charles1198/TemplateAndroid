package com.charles.common.util;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.charles.common.kv.Kv;

/**
 * @author charles
 * @date 2018/11/9
 * @description
 */
public class AlertUtil {

    /**
     * 弹出一个简单的 alert，只显示"我知道了"按钮
     *
     * @param title
     * @param message
     */
    public static void simpleAlert(Context context, String title, String message) {
        simpleAlert(context, title, message, "我知道了");
    }

    /**
     * 弹出一个简单的 alert
     *
     * @param context
     * @param title
     * @param message
     * @param buttonText
     */
    public static void simpleAlert(Context context, String title, String message, String buttonText) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, null)
                .create().show();
    }

    /**
     * 需要用户确认的alert，有确认按钮、取消按钮、"不再提示"按钮
     *
     * @param context
     * @param title
     * @param message
     * @param positiveLabel
     * @param negativeLabel
     * @param cancelable
     * @param noLongerTipsTag
     * @param listener
     */
    public static void showAlert(Context context, String title, String message, String positiveLabel, String negativeLabel,
                                 boolean cancelable, @Nullable String noLongerTipsTag, final SingleButtonAlertListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveLabel, (dialogInterface, i) -> listener.clickPositiveButton())
                .setNegativeButton(negativeLabel, null);
        if (noLongerTipsTag != null) {
            dialog.setView(checkBoxContainer(context, noLongerTipsTag));
        }
        dialog.create().show();
    }

    private static FrameLayout checkBoxContainer(Context context, final String noLongerTipsTag) {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setText("不再提示");
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> Kv.setBool(noLongerTipsTag, isChecked));

        FrameLayout container = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 48;
        container.addView(checkBox, layoutParams);
        return container;
    }

    public interface SingleButtonAlertListener {
        /**
         * 用户点击了"是的"按钮
         */
        void clickPositiveButton();
    }
}
