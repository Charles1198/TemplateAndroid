package com.charles.user.function.feedback;

import android.graphics.Bitmap;

/**
 * @author charles
 * @date 2018/10/17
 * @description
 */
public interface FeedbackView {
    /**
     * 获取反馈内容
     *
     * @return
     */
    String getFeedback();

    /**
     * 获取反馈图片
     *
     * @return
     */
    Bitmap getFeedbackImage();

    /**
     * 反馈成功
     */
    void submitFeedbackSucceed();

    /**
     * 反馈失败
     *
     * @param message
     */
    void submitFeedbackFailed(String message);


}
