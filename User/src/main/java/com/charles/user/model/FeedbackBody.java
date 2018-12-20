package com.charles.user.model;

import com.charles.common.Constant;
import com.charles.common.kv.Kv;
import com.charles.common.util.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author charles
 * @date 2018/11/14
 * @description
 */
public class FeedbackBody {
    private String shortName;
    private String appVersion;
    private String deviceUUID;
    private String userInfo;
    private String content;

    public FeedbackBody() {
        this.shortName = Constant.APP_NAME;
        this.appVersion = AppUtil.getAppVersion();
        this.deviceUUID = Kv.getString(Constant.UUID);
        this.userInfo = "{\n" +
                "    \"version\":\"1.0\",\n" +
                "    \"username\":\"\",\n" +
                "    \"email\":\"\",\n" +
                "    \"tel\":\"\"\n" +
                "}";
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("shortName", shortName);
            jsonObject.putOpt("appVersion", appVersion);
            jsonObject.putOpt("deviceUUID", deviceUUID);
            jsonObject.putOpt("userInfo", userInfo);
            jsonObject.putOpt("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
