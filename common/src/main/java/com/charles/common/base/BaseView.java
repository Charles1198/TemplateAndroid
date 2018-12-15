package com.charles.common.base;

import android.support.annotation.Nullable;

/**
 *
 * @author charles
 * @date 2017/2/18
 */

public interface BaseView {

    /**
     * 网络未连接
     */
    void noNetwork();

    void showLoading(@Nullable String message);

    void hideLoading();
}
