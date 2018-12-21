package com.charles.common.base;

import android.support.annotation.Nullable;

/**
 * @author charles
 * @date 2017/2/18
 */

public interface BaseView {

    /**
     * 网络未连接
     */
    void noNetwork();

    /**
     * 显示加载视图
     *
     * @param message 加载提示文字，可以为空
     */
    void showLoading(@Nullable String message);

    /**
     * 关闭加载视图
     */
    void hideLoading();
}
