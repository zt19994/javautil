package com.util.dateutil;

import com.util.traceutil.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author zt1994 2019/7/15 17:10
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static final String DATE_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT2 = "yyyy-MM-dd";
    private static final String DATE_FORMAT3 = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static final String DATE_FORMAT4 = "yyyyMMdd'T'HHmmss";

    private static final Integer TEN = 10;


    /**
     * Thu Aug 30 16:35:19 CST 2018 转化为String类型
     *
     * @return
     */
    public static String dateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
        return sdf.format(date);
    }


    /**
     * 根据传入的数据格式转换时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * 指定转化日期格式
     *
     * @param date
     * @return
     */
    public static String dateToStr4(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT4);
        return sdf.format(date);
    }


    /**
     * 字符转时间
     *
     * @param strToDate
     * @return
     */
    public static Date strToDate(String strToDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT1);
        Date date = null;
        try {
            date = simpleDateFormat.parse(strToDate);
        } catch (ParseException e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
        return date;
    }


    /**
     * 获取今天的时间，精确到秒
     *
     * @return
     */
    public static Date nowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT1);
        Date date = null;
        try {
            date = sdf.parse(sdf.format(new Date()));
            return date;
        } catch (ParseException e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
            return null;
        }
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


    /**
     * 校验开始和结束时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Boolean checkStartAndEndTime(String startTime, String endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        Date start = DateUtil.strToDate(startTime);
        Date end = DateUtil.strToDate(endTime);
        // 会议开始和结束时间有误
        if (end.before(new Date()) || start.after(end)) {
            return false;
        }
        return true;
    }


    /**
     * 校验结束时间
     *
     * @param endTime
     * @return
     */
    public static Boolean checkEndTime(String endTime) {
        if (endTime == null) {
            return false;
        }

        Date end = DateUtil.strToDate(endTime);
        if (end.before(new Date())) {
            return false;
        }
        return true;
    }


    /**
     * 获取传入时间的日份
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }


    /**
     * 获取传入时间的月份
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }


    /**
     * 获取传入时间的年份
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }


    /**
     * 获取传入时间的小时
     */
    public static int getHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * 获取传入时间的分钟
     */
    public static int getMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }


    /**
     * 获取传入时间的描
     */
    public static int getSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.SECOND);
    }


    /**
     * 获取传入的年月份，按格式输出
     */
    public static String getDate(Date date) {
        int year = DateUtil.getYear(date);
        int month = DateUtil.getMonth(date);
        int day = DateUtil.getDay(date);
        return year + "/" + month + "/" + day;
    }


    /**
     * 获取传入的时分秒，按格式输出
     */
    public static String getTime(Date date) {
        int hour = DateUtil.getHour(date);
        int minute = DateUtil.getMinute(date);
        int second = DateUtil.getSecond(date);
        String hourStr = hour + "";
        String minuteStr = minute + "";
        String secondStr = second + "";
        if (hour < TEN) {
            hourStr = "0" + hour;
        }
        if (minute < TEN) {
            minuteStr = "0" + minute;
        }
        if (second < TEN) {
            secondStr = "0" + second;
        }
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

}
