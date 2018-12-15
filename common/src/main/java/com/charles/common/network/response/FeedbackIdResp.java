package com.charles.common.network.response;

/**
 * @author charles
 * @date 2018/11/14
 * @description
 */
public class FeedbackIdResp {
    private String appFeedbackUUID;

    public String getAppFeedbackUUID() {
        return appFeedbackUUID == null ? "" : appFeedbackUUID;
    }

    public void setAppFeedbackUUID(String appFeedbackUUID) {
        this.appFeedbackUUID = appFeedbackUUID;
    }
}
