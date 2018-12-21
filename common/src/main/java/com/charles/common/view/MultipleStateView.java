package com.charles.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.charles.common.R;

/**
 * @author charles
 * @date 2018/10/23
 * @description
 */
public class MultipleStateView {

    private static final String EMPTY_MESSAGE = "列表中没有数据";
    private static final String NO_NETWORK_MESSAGE = "您的手机没有联网，请联网后再试";
    private static final String NETWORK_ERROR_MESSAGE = "网络连接错误，请检查您的网络状态";

    /**
     * loading
     *
     * @param context
     * @return
     */
    public static View loadingView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_loading, null);
    }

    /**
     * 空数据页面
     *
     * @param context
     * @return
     */
    public static View emptyView(Context context) {
        return emptyView(context, EMPTY_MESSAGE);
    }

    public static View emptyView(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_empty, null);
        TextView messageTv = view.findViewById(R.id.empty_message);
        messageTv.setText(message);
        return view;
    }

    /**
     * 网络故障
     *
     * @param context
     * @param listener
     * @return
     */
    public static View networkErrorView(Context context, final ErrorViewReloadListener listener) {
        return networkErrorView(context, NETWORK_ERROR_MESSAGE, listener);
    }

    public static View networkErrorView(Context context, String message, final ErrorViewReloadListener listener) {
        View errorView = LayoutInflater.from(context).inflate(R.layout.item_error, null);
        TextView messageTv = errorView.findViewById(R.id.error_message);
        messageTv.setText(message);
        errorView.findViewById(R.id.error).setOnClickListener(v -> listener.reload());
        return errorView;
    }

    /**
     * 未联网
     *
     * @param context
     * @param listener
     * @return
     */
    public static View noNetworkView(Context context, final ErrorViewReloadListener listener) {
        return noNetworkView(context, NO_NETWORK_MESSAGE, listener);
    }

    public static View noNetworkView(Context context, String message, final ErrorViewReloadListener listener) {
        View errorView = LayoutInflater.from(context).inflate(R.layout.item_no_network, null);
        TextView messageTv = errorView.findViewById(R.id.error_message);
        messageTv.setText(message);
        errorView.findViewById(R.id.error).setOnClickListener(v -> {
            listener.reload();
        });
        return errorView;
    }

    /**
     * 点击重试
     */
    public interface ErrorViewReloadListener {
        /**
         * 点击重试按钮
         */
        void reload();
    }
}
