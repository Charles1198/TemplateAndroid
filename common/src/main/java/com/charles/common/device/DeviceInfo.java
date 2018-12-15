package com.charles.common.device;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author charles
 * @date 16/10/22
 * 手机硬件信息
 */

public class DeviceInfo {
    private String deviceID;
    private String model;
    private String size;
    private String cpu;
    private String memory;

    public DeviceInfo(String deviceID, String model, String size, String cpu, String memory) {
        this.deviceID = deviceID;
        this.model = model;
        this.size = size;
        this.cpu = cpu;
        this.memory = memory;
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("deviceID", deviceID);
            jsonObject.putOpt("model", model);
            jsonObject.putOpt("size", size);
            jsonObject.putOpt("cpu", cpu);
            jsonObject.putOpt("memory", memory);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
