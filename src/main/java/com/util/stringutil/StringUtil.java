package com.util.stringutil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author zhongtao on 2019/1/7
 */
public class StringUtil {
    /**
     * 无参构造方法
     */
    private StringUtil() {
    }

    /**
     * 有参构照方法
     */
    public static String fromUnicode(String str) {

        return fromUnicode(str.toCharArray(), 0, str.length(), new char[1024]);

    }

    public static boolean isEmpty(String s) {
        if (s == null || s.length() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    //金额验证
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 针对敏感信息（如手机号等）屏蔽字符串
     *
     * @param s
     * @return
     */
    public static String maskString(String s) {
        if (isEmpty(s)) {
            return "";
        }
        if (s.length() == 1) {
            return "*";
        } else if (s.length() == 2) {
            return s.substring(0, 1) + "*";
        } else {
            return s.substring(0, 1) + "***" + s.substring(s.length() - 1, s.length());
        }
    }

    /**
     * 将unicode编码转换成String Converts encoded &#92;uxxxx1 to unicode chars
     * <p>
     * and changes special saved chars to their original forms
     */

    public static String fromUnicode(char[] in, int off, int len, char[] convtBuf) {
        char[] convtBufNew = convtBuf;
        if (convtBufNew.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBufNew = new char[newLen];
        }
        char aChar;
        char[] out = convtBufNew;
        int outLen = 0;
        int end = off + len;
        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = (char) aChar;
            }
        }
        return new String(out, 0, outLen);
    }
}
