package com.util.iputil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * ip 掩饰码匹配工具类
 *
 * @author zt1994 2019/10/15 17:31
 */
public class IpMaskUtil {

    private static final Logger logger = LoggerFactory.getLogger(IpMaskUtil.class);


    /**
     * 号码匹配规则
     */
    private static Pattern MASK_PATTERN = Pattern.compile("(^((\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])$)|^(\\d|[1-2]\\d|3[0-2])$");


    /**
     * 校验是否是掩饰码
     *
     * @param mask
     * @return
     */
    public static boolean isMask(String mask) {
        return MASK_PATTERN.matcher(mask).matches();
    }


    /**
     * 将子网掩码转换成ip子网掩码形式，比如输入"255.255.255.255" 或者32统一输出为255.255.255.255
     *
     * @param mask
     * @return
     */
    public static String mask2ipMask(String mask) {
        String ipMask = null;
        // 如果不是子网掩码
        if (!isMask(mask)) {
            logger.error("ip掩码格式(" + mask + ")不正确");
        } else if (mask.contains(".")) {
            // 如果是ip类型的子网掩码[0.0.0.0,255.255.255.255]则直接返回
            ipMask = mask;
            // 如果是数字类型的子网掩码[0,32]
        } else {
            int iNetMask = Integer.valueOf(mask);
            int part = iNetMask / 8;
            int remainder = iNetMask % 8;
            int sum = 0;
            for (int i = 8; i > 8 - remainder; i--) {
                sum = sum + (int) Math.pow(2, i - 1);
            }
            if (part == 0) {
                ipMask = sum + ".0.0.0";
            } else if (part == 1) {
                ipMask = "255." + sum + ".0.0";
            } else if (part == 2) {
                ipMask = "255.255." + sum + ".0";
            } else if (part == 3) {
                ipMask = "255.255.255." + sum;
            } else if (part == 4) {
                ipMask = "255.255.255.255";
            }
        }
        return ipMask;
    }


    /**
     * 数字转掩饰码证书
     *
     * @param n
     * @return
     */
    public static int findNumber(int n) {
        int countOf = 0;
        int tag = 1;
        while (tag != 0) {
            if ((tag & n) != 0) {
                countOf++;
            }
            tag = tag << 1;
        }
        return countOf;
    }


    /**
     * 检查ip是否合法
     *
     * @param text
     * @return
     */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                return true;
                // 返回判断信息
                //return text + "\n是一个合法的IP地址！";
            } else {
                return false;
                // 返回判断信息
                // return text + "\n不是一个合法的IP地址！";
            }
        }
        return false;
        // 返回判断信息
        //	return "请输入要验证的IP地址！";
    }


}
