package com.charles.common.error.model;

import android.os.Build;

import com.charles.common.Constant;
import com.charles.common.device.GetDeviceInfo;
import com.charles.common.network.NetworkUtil;
import com.charles.common.util.DateUtil;
import com.charles.common.util.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * @author charles
 * @date 16/10/24
 */

public class ExceptionInfo {
    /**
     * shortName : test
     * exceptionType : 1
     * appVersion : 0.0.1
     * deviceUUID : 620000200005312946
     * phoneNetwork : 1
     * OSRelease : Android@6.1
     * exceptionReason : 6xniu6
     * exceptionTrace : sJEygw
     * exceptionAt : 1978-02-09 14:24:21
     */

    private String shortName;
    private String exceptionType;
    private String appVersion;
    private String deviceUUID;
    private String phoneNetwork;
    private String OSRelease;
    private String exceptionReason;
    private String exceptionTrace;
    private String threadName;
    private String threadTrace;
    private String exceptionAt;

    public ExceptionInfo(Thread th, Throwable ex) {
        shortName = Constant.APP_NAME;
        exceptionType = "1";
        appVersion = AppUtil.getAppVersion();
        deviceUUID = GetDeviceInfo.getDeviceId();
        phoneNetwork = NetworkUtil.getNetworkState();
        OSRelease = Build.VERSION.RELEASE;
        exceptionReason = ex.getMessage();
        exceptionTrace = Arrays.toString(ex.getStackTrace());
        threadName = th.getName();
        threadTrace = Arrays.toString(th.getStackTrace());
        exceptionAt = DateUtil.getCurTime();
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("shortName", shortName);
            jsonObject.putOpt("exceptionType", exceptionType);
            jsonObject.putOpt("appVersion", appVersion);
            jsonObject.putOpt("deviceUUID", deviceUUID);
            jsonObject.putOpt("phoneNetwork", phoneNetwork);
            jsonObject.putOpt("OSRelease", OSRelease);
            jsonObject.putOpt("exceptionReason", exceptionReason);
            jsonObject.putOpt("exceptionTrace", exceptionTrace);
            jsonObject.putOpt("threadName", threadName);
            jsonObject.putOpt("threadTrace", threadTrace);
            jsonObject.putOpt("exceptionAt", exceptionAt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
