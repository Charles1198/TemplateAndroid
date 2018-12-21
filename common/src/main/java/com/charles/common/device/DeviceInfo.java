package com.charles.common.device;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author charles
 * @date 16/10/22
 * 手机硬件信息
 */

public class DeviceInfo {
    /**
     * 设备唯一Id，
     */
    private String deviceID;
    /**
     * 设备型号
     */
    private String model;
    /**
     *  设备屏幕尺寸
     */
    private String size;
    /**
     * 设备cpu型号
     */
    private String cpu;
    /**
     * 设备存储容量
     */
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
