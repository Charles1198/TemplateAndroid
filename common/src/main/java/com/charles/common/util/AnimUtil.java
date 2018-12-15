package com.charles.common.util;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * @author charles
 * @date 2018/10/4
 * @description
 */
public class AnimUtil {
    /**
     * 透明度动画
     *
     * @param view      目标View
     * @param fromValue 起始值
     * @param toValue   结束值
     * @param duration  持续时间
     */
    public static void alphaAnim(View view, float fromValue, float toValue, long duration) {
        alphaAnim(view, fromValue, toValue, duration, true);
    }

    /**
     * 透明度动画
     *
     * @param view      目标View
     * @param fromValue 起始值
     * @param toValue   结束值
     * @param duration  持续时间
     * @param fillAfter 动画结束后是否维持结束状态
     */
    public static void alphaAnim(View view, float fromValue, float toValue, long duration, boolean fillAfter) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromValue, toValue);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(fillAfter);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 缩放动画
     *
     * @param view      目标View
     * @param fromValue 起始值
     * @param toValue   结束值
     * @param duration  持续时间
     */
    public static void scaleAnim(View view, float fromValue, float toValue, long duration) {
        scaleAnim(view, fromValue, toValue, duration, Animation.REVERSE);
    }

    /**
     * 缩放动画
     *
     * @param view       目标View
     * @param fromValue  起始值
     * @param toValue    结束值
     * @param duration   持续时间
     * @param repeatMode 动画重复模式
     */
    public static void scaleAnim(View view, float fromValue, float toValue, long duration, int repeatMode) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromValue, toValue, fromValue, toValue,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setRepeatMode(repeatMode);
        view.startAnimation(scaleAnimation);
    }

    /**
     * 缩放动画
     *
     * @param view      目标View
     * @param fromValue 起始值
     * @param toValue   结束值
     * @param duration  持续时间
     * @param fill      动画结束后是否维持结束状态
     */
    public static void scaleAnim(View view, float fromValue, float toValue, long duration, boolean fill) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromValue, toValue, fromValue, toValue,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(fill);
        view.startAnimation(scaleAnimation);
    }

    public static void transformAnim(View view, float fromX, float toX, float fromY, float toY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration(800);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setRepeatCount(100);
        view.startAnimation(translateAnimation);
    }
}
