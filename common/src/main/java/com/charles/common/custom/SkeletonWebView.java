package com.charles.common.custom;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.charles.common.R;
import com.charles.common.util.AnimUtil;

/**
 * @author charles
 * @date 2018/11/1
 * @description
 */
public class SkeletonWebView extends FrameLayout {
    private static final String SHADOW_HTML_HEAD = "<div style='border-radius: 6px;box-shadow: 0 2px 6px #00000030;padding:10px'>";
    private static final String SHADOW_HTML_FOOT = "</div>";

    private FrameLayout fixView;
    private FrameLayout moveView;
    private WebView webView;

    public SkeletonWebView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_skeleton_webview, this);

        fixView = view.findViewById(R.id.skeleton_fix_view);
        moveView = view.findViewById(R.id.skeleton_move_view);
        webView = view.findViewById(R.id.skeleton_webView);

        AnimUtil.transformAnim(moveView, 0, 2000, 0, 0);
    }

    public SkeletonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 为 WebView 加载 html 字符串
     *
     * @param data
     */
    public void loadStringData(String data) {
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
        watchWebViewLoad();
    }

    public void loadStringDataWithShadow(String data) {
        data = SHADOW_HTML_HEAD + data + SHADOW_HTML_FOOT;
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
        watchWebViewLoad();
    }

    private void watchWebViewLoad() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideSkeleton();
            }
        });
    }

    private void hideSkeleton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fixView.setVisibility(GONE);
                moveView.clearAnimation();
                moveView.setVisibility(GONE);
            }
        }, 500);
    }
}
