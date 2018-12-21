package com.charles.common.kv;

import com.tencent.mmkv.MMKV;

/**
 * @author charles
 * @date 2018/10/1
 * @description 存储key-value数据对，类似于SharedPreferences
 */
public class Kv {

    /**
     * 检查数据是否存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.contains(key);
    }

    /**
     * 存数据
     *
     * @param key
     * @param value
     */
    public static void setBool(String key, boolean value) {
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.encode(key, value);
    }

    /**
     * 取数据
     *
     * @param key
     * @return
     */
    public static boolean getBool(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeBool(key, false);
    }

    /**
     * 取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBool(String key, boolean defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeBool(key, defaultValue);
    }

    /**
     * 存数据
     *
     * @param key
     * @param value
     */
    public static void setInt(String key, int value) {
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.encode(key, value);
    }

    /**
     * 取数据
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeInt(key, 0);
    }

    /**
     * 取数据
     *
     * @param key
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeInt(key, defaultValue);
    }

    /**
     * 存数据
     *
     * @param key
     * @param value
     */
    public static void setString(String key, String value) {
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.encode(key, value);
    }

    /**
     * 取数据
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeString(key, "");
    }

    /**
     * 删除数据
     *
     * @param key
     */
    public static void remove(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.remove(key);
    }
}
