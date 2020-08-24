package com.util.jsonutil;

import com.alibaba.fastjson.JSONObject;
import com.util.traceutil.StackTraceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 获取请求 request 的json工具类
 *
 * @author zt1994 2020/3/2 16:40
 */
public class GetRequestJsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(GetRequestJsonUtils.class);

    private static final String GET_METHOD = "GET";


    /**
     * 获取请求json类
     *
     * @param request
     * @return
     */
    public static JSONObject getRequestJsonObject(HttpServletRequest request) {
        String json = null;
        try {
            json = getRequestJsonString(request);
        } catch (IOException e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
        return JSONObject.parseObject(json);
    }

    /***
     * 获取 request 中 json 字符串的内容
     *
     * @param request
     * @return : <code>byte[]</code>
     * @throws IOException
     */
    public static String getRequestJsonString(HttpServletRequest request) throws IOException {
        String requestMethod = request.getMethod();
        if (StringUtils.equals(requestMethod, GET_METHOD)) {
            return new String(request.getQueryString()
                    .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
                    .replaceAll("%22", "\"");
        } else {
            return getRequestPostStr(request);
        }
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readLen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readLen == -1) {
                break;
            }
            i += readLen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest request) throws IOException {
        byte[] buffer = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        if (buffer != null) {
            return new String(buffer, charEncoding);
        } else {
            return "";
        }
    }
}
