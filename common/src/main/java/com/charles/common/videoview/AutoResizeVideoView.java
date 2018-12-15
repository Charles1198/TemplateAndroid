package com.charles.common.videoview;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * @author charles
 * @date 2018/11/29
 * @description 简单自定义的VideoView，随着屏幕方向切换自动调整大小和位置。竖屏视频和屏幕等宽在顶部，横屏视频和屏幕等高在中间。
 */
public class AutoResizeVideoView extends VideoView {
    private Context context;
    private CustomMediaController mediaController;

    /**
     * videoView原本尺寸
     */
    private int videoRawWidth = 0;
    private int videoRawHeight = 0;

    /**
     * 每秒钟刷新一次播放进度
     */
    private Handler handler = new Handler();
    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            mediaController.setProgress();
            handler.postDelayed(mShowProgress, 1000);
        }
    };

    public AutoResizeVideoView(Context context) {
        super(context);
        this.context = context;
    }

    public AutoResizeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void setVideoURI(Uri uri) {
        super.setVideoURI(uri);

        mediaController = new CustomMediaController(context);
        mediaController.setAnchorView(this);
        this.setMediaController(mediaController);
        this.setOnPreparedListener(mp -> {
            start();
            mediaController.setPlayBtnStatus();
            handler.postDelayed(mShowProgress, 1000);
            mp.setOnVideoSizeChangedListener((mp1, width, height) -> {
                videoRawWidth = width;
                videoRawHeight = height;
            });
        });
    }

    /**
     * 重写start()、pause()、onDetachedFromWindow()方法，控制定时器的开启/关闭
     */
    @Override
    public void start() {
        super.start();
        handler.postDelayed(mShowProgress, 1000);
    }

    @Override
    public void pause() {
        super.pause();
        handler.removeCallbacks(mShowProgress);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(mShowProgress);
    }

    /**
     * 屏幕方向发生改变，调整视图尺寸
     *
     * @param orientation
     */
    public void orientationChanged(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.getLayoutParams();
            layoutParams.leftMargin = 0;
            this.setLayoutParams(layoutParams);
        } else {
            // 横屏
            DisplayMetrics dm = this.getResources().getDisplayMetrics();
            // 视频下面有1像素的白边
            int realHeight = dm.heightPixels + 1;
            int realWidth = (int) (realHeight * (float) videoRawWidth / (float) videoRawHeight);
            int marginLeft = (dm.widthPixels - realWidth) / 2;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.getLayoutParams();
            layoutParams.height = realHeight;
            layoutParams.width = realWidth;
            layoutParams.leftMargin = marginLeft;
            this.setLayoutParams(layoutParams);
        }
    }
}
