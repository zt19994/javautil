package com.util.traceutil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 获取错误信息工具类
 *
 * @author zt1994 2020/3/2 15:51
 */
public class StackTraceUtil {

    /**
     * 获取e.printStackTrace() 的具体信息，赋值给String 变量，并返回
     *
     * @param e Exception
     * @return e.printStackTrace() 中 的信息
     */
    public static String getStackTraceInfo(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            return sw.toString();
        } catch (Exception ex) {
            return "printStackTrace()转换错误";
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
    }
}
