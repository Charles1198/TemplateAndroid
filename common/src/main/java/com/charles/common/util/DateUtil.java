package com.charles.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author charles
 * @date 16/11/2
 */

public class DateUtil {
    /**
     * 获取当前时间
     * 时间格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String getCurStrTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return format.format(curDate);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurHour() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static String getCurYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 获取从指定日期起几天后日期
     *
     * @return
     */
    public static String getDateAfterDaysFromDate(String date, int days) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date d = formatter.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
            return formatter.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateAfterDaysFromDate(String date, double days) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long millis = 0;
        try {
            Date d = formatter.parse(date);
            millis = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date targetDate = new Date((long) (millis + 1000 * 60 * 60 * 24 * days));
        return formatter.format(targetDate);
    }

    /**
     * 获取从指定日期的毫秒值
     *
     * @return
     */
    public static long getMillisAtDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long millis = 0;
        try {
            Date d = formatter.parse(date);
            millis = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    public static int diffOfTimeByDay(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long time1 = 0;
        try {
            Date d = formatter.parse(date);
            time1 = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time2 = System.currentTimeMillis();
        return (int) ((time2 - time1) / (1000 * 60 * 60 * 24));
    }

    /**
     * 从今天到 date 日期间隔天数
     */
    public static int diffOfDateByDay(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        long time1 = 0;
        try {
            Date d = formatter.parse(date);
            time1 = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time2 = System.currentTimeMillis();
        return (int) ((time1 - time2) / (1000 * 60 * 60 * 24));
    }

    /**
     * 比较日期先后
     *
     * @param time1
     * @param time2
     * @return true:time1日期早于time2
     */
    public static boolean isDateBefore(String time1, String time2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date a = null;
        Date b = null;
        try {
            a = dateFormat.parse(time1);
            b = dateFormat.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return a != null && b != null && a.before(b);
    }

    /**
     * 比较时间先后
     */
    public static boolean isTimeBefore(String time1, String time2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        Date a = null;
        Date b = null;
        try {
            a = dateFormat.parse(time1);
            b = dateFormat.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return a != null && b != null && a.before(b);
    }

    /**
     * 判断年份是否为闰年
     * 判断闰年的条件， 能被4整除同时不能被100整除，或者能被400整除
     */
    public static boolean isLeapYear(int year) {

        boolean isLeapYear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            isLeapYear = true;
        } else if (year % 400 == 0) {
            isLeapYear = true;
        }
        return isLeapYear;
    }

    /**
     * 将 xxxx-xx-xx 格式的日期写成 x月x日
     *
     * @param string
     * @return
     */
    public static String formatDateFrom(String string) {
        String[] dates = string.split("-");
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        return month + "月" + day + "日";
    }
}
