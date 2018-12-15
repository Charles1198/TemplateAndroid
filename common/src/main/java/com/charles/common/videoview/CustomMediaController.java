package com.charles.common.videoview;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.charles.common.R;

/**
 * @author charles
 * @date 2018/11/25
 * @description
 */
public class CustomMediaController extends MediaController implements View.OnClickListener {
    public static String REQUEST_FULL_SCREEN_ACTION = "requestFullScreenAction";
    public static String SCREEN_ORIENTATION = "screenOrientation";

    private Context context;

    private MediaPlayerControl mediaPlayerControl;
    private ImageView playBtn;
    private ImageView fullScreenBtn;
    private SeekBar seekBar;
    private TextView progressTv;

    private boolean mDragging = false;
    private int videoDuration = 0;
    private int videoScreenOrientation = Configuration.ORIENTATION_PORTRAIT;


    public CustomMediaController(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        mediaPlayerControl = player;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        removeAllViews();
        View customView = LayoutInflater.from(context).inflate(R.layout.view_media_controller, null);
        addView(customView);

        initControllerView(customView);
    }

    private void initControllerView(View customView) {
        playBtn = customView.findViewById(R.id.mc_play);
        fullScreenBtn = customView.findViewById(R.id.mc_fullScreen_btn);
        seekBar = customView.findViewById(R.id.mc_progress);
        progressTv = customView.findViewById(R.id.mc_progress_tv);

        playBtn.setOnClickListener(this);
        fullScreenBtn.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mDragging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDragging = false;
                mediaPlayerControl.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.mc_play) {
            if (mediaPlayerControl.isPlaying()) {
                mediaPlayerControl.pause();
            } else {
                mediaPlayerControl.start();
            }
            setPlayBtnStatus();
        } else if (viewId == R.id.mc_fullScreen_btn) {
            Intent intent = new Intent();
            intent.setAction(REQUEST_FULL_SCREEN_ACTION);
            if (videoScreenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                videoScreenOrientation = Configuration.ORIENTATION_LANDSCAPE;
                fullScreenBtn.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_fullscreen_close));
            } else {
                videoScreenOrientation = Configuration.ORIENTATION_PORTRAIT;
                fullScreenBtn.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_fullscreen));
            }
            intent.putExtra(SCREEN_ORIENTATION, videoScreenOrientation);
            context.sendBroadcast(intent);
        }
    }

    public void setPlayBtnStatus() {
        if (mediaPlayerControl.isPlaying()) {
            playBtn.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_pause_black_24dp));
        } else {
            playBtn.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_play_arrow_black_24dp));
        }
    }

    /**
     * 显示播放进度，视频时长
     */
    public void setProgress() {
        if (mDragging) {
            return;
        }

        if (videoDuration == 0) {
            videoDuration = mediaPlayerControl.getDuration();
        }
        seekBar.setMax(videoDuration);

        int position = mediaPlayerControl.getCurrentPosition();
        seekBar.setProgress(position);

        int percent = mediaPlayerControl.getBufferPercentage();
        seekBar.setSecondaryProgress(percent * videoDuration);

        progressTv.setText(String.format("%s / %s", stringForTime(position), stringForTime(videoDuration)));
    }

    /**
     * 毫秒数转换成hh:mm:ss形式
     *
     * @param timeMs
     * @return
     */
    @SuppressLint("DefaultLocale")
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
