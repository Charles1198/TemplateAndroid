package com.charles.common.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * @author charles
 * @date 2018/10/7
 * @description
 */
public class NoTouchWebView extends WebView {

    private static final String SHADOW_HTML_HEAD = "<div style='border-radius: 6px;box-shadow: 0 2px 6px #00000030;padding:10px'>";
    private static final String SHADOW_HTML_FOOT = "</div>";

    public NoTouchWebView(Context context) {
        super(context);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    public NoTouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setVerticalScrollBarEnabled(false);
    }

    /**
     * 为 WebView 加载 html 字符串
     *
     * @param data
     */
    public void loadStringData(String data) {
        this.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

    public void loadStringDataWithShadow(String data) {
        data = SHADOW_HTML_HEAD + data + SHADOW_HTML_FOOT;
        this.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
