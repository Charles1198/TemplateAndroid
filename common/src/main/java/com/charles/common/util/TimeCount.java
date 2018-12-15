package com.charles.common.util;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 *
 * @author charles
 * @date 2017/1/2
 * 设计倒计时
 */

public class TimeCount extends CountDownTimer {
    private Button bt;
    private String finishText;

    /**
     * @param millisInFuture    计时总时长
     * @param countDownInterval 时间间隔
     * @param bt                目标
     * @param finishText        结束计时显示的文字
     */
    public TimeCount(long millisInFuture, long countDownInterval, Button bt, String finishText) {
        //为毛是 * 1.001，因为timeCount貌似计时不准确，大概2-3秒多1毫秒
        super(millisInFuture, countDownInterval);
        this.bt = bt;
        this.finishText = finishText;

    }

    @Override
    public void onFinish() {//计时完毕时触发
        bt.setText(finishText);
        bt.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {//计时过程显示
        bt.setEnabled(false);
        bt.setText((millisUntilFinished / 1000) + "秒后重试");
    }
}
