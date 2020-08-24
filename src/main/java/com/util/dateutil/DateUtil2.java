package com.util.dateutil;

import com.util.logutil.MyLogger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式化
 *
 * @author zhongtao on 2019/1/4
 */
public class DateUtil2 {

    /**
     * 记录日志对象
     */
    public static final MyLogger LOGGER = new MyLogger(DateUtil2.class);

    private static final String DATE = "yyyy-MM-dd";

    /**
     * 私有化构造方法，防止外部调用
     */
    private DateUtil2() {
    }

    /**
     * 返回String字符串 格式：yyyy-MM-dd HH:mm:ss
     *
     * @param date 要格式化的日期
     * @return String 格式化后的日期
     */
    public static String dataFormat(Date date) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        return time.format(date);
    }

    /**
     * 返回String字符串 格式：yyyy-MM-dd
     *
     * @param date 要格式化的日期
     * @return String 格式化后的日期
     */
    public static String todayFormat(Date date) {
        SimpleDateFormat time = new SimpleDateFormat(DATE);
        return time.format(date);
    }

    /**
     * 返回String字符串 格式：yyyy-MM-dd
     *
     * @param date 要格式化的日期
     * @return String 格式化后的日期
     */
    public static String todayFormatString(Date date) {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");
        return time.format(date);
    }

    /**
     * 返回String字符串 格式：yyyy-MM-dd
     *
     * @param date 要格式化的日期
     * @return String 格式化后的日期
     */
    public static int todayFormatInt(Date date) {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(time.format(date));
    }

    /**
     * 返回String字符串 格式：yyyy-MM-dd
     *
     * @param date 要格式化的日期
     * @return String 格式化后的日期
     */
    public static String yesterdayFormat(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
    }

    /**
     * 当前时间的字符串
     *
     * @param date 时间
     * @return 时间的数字字符串格式
     */
    public static String mathString(Date date) {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.UK);
        return time.format(date);
    }

    public static String toNanosecond(Date date) {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);
        return time.format(date);
    }

    /**
     * String转Date
     *
     * @param dateString 要转化的date 字符串
     * @return 转换后的日期
     */
    public static Date stringToDate(String dateString) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = null;
        try {
            time = formatDate.parse(dateString);
        } catch (ParseException e) {
            LOGGER.error("String转Date失败！", e);
        }

        return time;
    }

    /**
     * String转Date
     *
     * @param dateString 要转化的date 字符串
     * @return
     */
    public static Date stringToDateMM(String dateString) {
        SimpleDateFormat formatDate = new SimpleDateFormat(DATE);
        Date time = null;
        try {
            time = formatDate.parse(dateString);
        } catch (ParseException e) {
            LOGGER.error("String转Date失败！", e);
        }
        return time;
    }

    /**
     * 当前时间加几天
     *
     * @param number 当亲日期后的第number天
     * @return String 当亲日期后的第number天的额日期
     */
    public static String nextNumberDate(int number) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date dd = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dd);
        calendar.add(Calendar.DAY_OF_MONTH, number);
        return format.format(calendar.getTime());
    }

    /**
     * 日期加一天
     *
     * @param s 要增加的日期
     * @param n 要增加的天数
     * @return String 增加n填后的日期
     */
    public static String addDay(String s, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE);

            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);// 增加一天
            // cd.add(Calendar.MONTH, n);//增加一个月

            return sdf.format(cd.getTime());
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    /**
     * 比较两个日期 大小
     *
     * @param nowDate 日期1
     * @param endDate 日期2
     * @return nowDate小于endDate 返回true，否则返回false
     */
    public static boolean compare_date(String nowDate, String endDate) {

        DateFormat df = new SimpleDateFormat(DATE);
        try {
            Date dt1 = df.parse(nowDate);
            Date dt2 = df.parse(endDate);
            if (dt1.getTime() <= dt2.getTime()) {
                return true;
            } else if (dt1.getTime() > dt2.getTime()) {
                return false;
            }
        } catch (Exception exception) {
            LOGGER.error("日期对比失败！", exception);
        }
        return false;
    }

    /**
     * 比较两个日期大小
     *
     * @param nowDate 日期1
     * @param endDate 日期2
     * @return nowDate小于endDate 返回true,否则返回false
     */
    public static boolean compare_date_pv(Date nowDate, Date endDate) {

        try {
            Date dt1 = nowDate;
            Date dt2 = endDate;
            if (dt1.getTime() <= dt2.getTime()) {
                return false;
            } else if (dt1.getTime() > dt2.getTime()) {
                return true;
            }
        } catch (Exception exception) {
            LOGGER.error("日期对比失败！", exception);
        }
        return false;
    }

    /**
     * 计算当前时间和参数(过去时间)之间间隔多少秒
     *
     * @param startDate 过去时间
     * @return 间隔多少秒
     */
    public static int intervalSecondToNow(Date startDate) {
        long nowTime = new Date().getTime();
        long startTime = startDate.getTime();
        return (int) (nowTime - startTime) / 1000;
    }


    /**
     * 秒数转为 时：分：秒 格式
     *
     * @param strSeconds 秒数
     * @return
     */
    public static String secondsToDate(String strSeconds) {
        int seconds = Integer.parseInt(strSeconds);
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = seconds % 60;
        seconds -= second;
        if (seconds > 0) {
            seconds /= 60;
            minute = seconds % 60;
            seconds -= minute;
            if (seconds > 0) {
                hour = seconds / 60;
            }
        }
        return hour + ":" + minute + ":" + second;
    }


    /**
     * 时：分：秒 格式 转为秒数
     *
     * @param time
     * @return
     */
    public static Integer DateToSeconds(String time) {
        int seconds = 0;
        String[] split = time.split(":");
        seconds = Integer.parseInt(split[0]) * 3600 + Integer.parseInt(split[1]) * 60 + Integer.parseInt(split[2]);
        return seconds;
    }
}
