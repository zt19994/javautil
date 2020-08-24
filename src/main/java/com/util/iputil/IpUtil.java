package com.util.iputil;

import com.alibaba.druid.util.StringUtils;
import com.util.traceutil.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ip 工具类
 *
 * @author zt1994 2019/7/15 18:04
 */
public class IpUtil {

    private static final Logger logger = LoggerFactory.getLogger("IpUtil");

    private final static String ERROR_IP = "127.0.0.1";

    private final static String UN_KNOWN = "unknown";

    private final static String ERROR_0_IP = "0:0:0:0:0:0:0:1";

    private final static Pattern PATTERN = Pattern.
            compile("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})");


    /**
     * 取外网 IP
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-real-ip");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        //过滤反向代理的ip
        String[] ipTemps = ip.split(",");
        if (ipTemps.length >= 1) {
            //得到第一个IP，即客户端真实IP
            ip = ipTemps[0];
        }

        ip = ip.trim();
        if (ip.length() > 23) {
            ip = ip.substring(0, 23);
        }

        return ip;
    }


    /**
     * 获取用户的真实ip
     *
     * @param request
     * @return
     */
    public static String getUserIP(HttpServletRequest request) {

        // 优先取X-Real-IP
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || StringUtils.equalsIgnoreCase(UN_KNOWN, ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || StringUtils.equalsIgnoreCase(UN_KNOWN, ip)) {
            ip = request.getRemoteAddr();
            if (StringUtils.equalsIgnoreCase(ERROR_0_IP, ip)) {
                ip = ERROR_IP;
            }
        }

        if (StringUtils.equalsIgnoreCase(UN_KNOWN, ip)) {
            ip = ERROR_IP;
            return ip;
        }

        int pos = ip.indexOf(',');
        if (pos >= 0) {
            ip = ip.substring(0, pos);
        }

        return ip;
    }

    public static String getLastIpSegment(HttpServletRequest request) {
        String ip = getUserIP(request);
        if (ip != null) {
            ip = ip.substring(ip.lastIndexOf('.') + 1);
        } else {
            ip = "0";
        }
        return ip;
    }


    /**
     * 校验 ip 是否有效
     *
     * @param request
     * @return
     */
    public static boolean isValidIP(HttpServletRequest request) {
        String ip = getUserIP(request);
        return isValidIP(ip);
    }


    /**
     * 判断我们获取的ip是否是一个符合规则ip
     *
     * @param ip
     * @return
     */
    public static boolean isValidIP(String ip) {
        if (StringUtils.isEmpty(ip)) {
            logger.debug("ip is null. valid result is false");
            return false;
        }

        Matcher matcher = PATTERN.matcher(ip);
        boolean isValid = matcher.matches();
        logger.debug("valid ip:" + ip + " result is: " + isValid);
        return isValid;
    }


    public static String getLastServerIpSegment() {
        String ip = getServerIP();
        if (ip != null) {
            ip = ip.substring(ip.lastIndexOf('.') + 1);
        } else {
            ip = "0";
        }
        return ip;
    }


    public static String getServerIP() {
        InetAddress internet;
        try {
            internet = InetAddress.getLocalHost();
            return internet.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
        return "127.0.0.1";
    }
}
