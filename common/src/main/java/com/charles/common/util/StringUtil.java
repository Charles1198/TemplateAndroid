package com.charles.common.util;

import android.util.Log;

import java.net.URLEncoder;

/**
 * @author charles
 * @date 2018/10/5
 * @description
 */
public class StringUtil {
    /**
     * 判断手机号合法性
     *
     * @param s
     * @return
     */
    public static boolean isTel(String s) {
        String regex = "^1[3578][0-9]{9}$";
        return s.matches(regex);
    }

    /**
     * 判断邮箱合法性
     *
     * @param s
     * @return
     */
    public static boolean isEmail(String s) {
        String regex = "^[a-zA-Z0-9_]{2,15}[@][a-z0-9]{2,}[.]\\p{Lower}{2,}$";
        return s.matches(regex);
    }

    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.e("toURLEncoded error:", localException.toString());
        }
        return "";
    }


}
