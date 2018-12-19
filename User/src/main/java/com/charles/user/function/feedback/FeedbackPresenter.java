package com.charles.user.function.feedback;

import android.graphics.Bitmap;

import com.charles.common.network.AbstractMyCallBack;
import com.charles.common.network.ApiManager;
import com.charles.common.network.NetworkUtil;
import com.charles.common.network.response.BaseResp;
import com.charles.common.network.response.FeedbackIdResp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * @author charles
 * @date 2018/10/17
 * @description
 */
class FeedbackPresenter {
    private FeedbackView feedbackView;
    private ApiManager apiManager;

    FeedbackPresenter(FeedbackView feedbackView) {
        this.feedbackView = feedbackView;
        apiManager = NetworkUtil.createApiManager();
    }

    void submitFeedback() {
        if (!NetworkUtil.isNetworkConnected()) {
            return;
        }

        String feedbackContent = feedbackView.getFeedback();
        FeedbackBody feedbackBody = new FeedbackBody();
        feedbackBody.setContent(feedbackContent);
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                feedbackBody.toJsonString());
        Call<BaseResp<FeedbackIdResp>> call = apiManager.submitFeedbackContent(requestBody);
        call.enqueue(new AbstractMyCallBack<BaseResp<FeedbackIdResp>>() {
            @Override
            public void onSuc(BaseResp<FeedbackIdResp> response) {
                String feedbackId = response.getData().getAppFeedbackUUID();
                submitFeedbackImage(feedbackId);
            }

            @Override
            public void onFail(String statusCode) {
                feedbackView.submitFeedbackFailed("");
            }

            @Override
            public void requestAgain() {
                submitFeedback();
            }
        });
    }

    private void submitFeedbackImage(final String feedbackId) {
        Bitmap bitmap = feedbackView.getFeedbackImage();
        if (bitmap == null) {
            // 没有图片
            feedbackView.submitFeedbackSucceed();
            return;
        }

        // 根据图片路径寻找图片并上传
        byte[] feedbackImageByte = getBitmapByte(bitmap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, image/jpeg"),
                feedbackImageByte);
        Call<BaseResp> call = apiManager.submitFeedbackImage(feedbackId, requestBody);
        call.enqueue(new AbstractMyCallBack<BaseResp>() {
            @Override
            public void onSuc(BaseResp response) {
                feedbackView.submitFeedbackSucceed();
            }

            @Override
            public void onFail(String statusCode) {
                feedbackView.submitFeedbackFailed("");
            }

            @Override
            public void requestAgain() {
                submitFeedbackImage(feedbackId);
            }
        });
    }

    /**
     * 将bitmap图片转换成二进制
     *
     * @param bitmap
     * @return
     */
    private byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
