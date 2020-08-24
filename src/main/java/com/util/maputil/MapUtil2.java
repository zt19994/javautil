package com.util.maputil;

import com.util.beanutil.ParamException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author LYP
 * @version 1.0
 * @date 2020/6/6 0006 下午 2:33
 * @description: 从map获取不为null的值
 */
public class MapUtil2 {

    /**
     * 把map里面的为null的value值转化为空串“”
     */
    public static Map<String, Object> nullToEmpty(Map<String, Object> map) {
        Set<String> set = map.keySet();
        if (set != null && !set.isEmpty()) {
            for (String key : set) {
                if (map.get(key) == null) {
                    map.put(key, "");
                }
            }
        }
        return map;
    }

    /**
     * 获取不为null的 字符串值
     */
    public static String getString(Map<String, Object> map, String key) {
        String value = "";
        if (null == map) {
            return value;
        }
        try {
            value = map.get(key) == null ? "" : map.get(key).toString().trim();
        } catch (Exception e) {
            throw new ParamException("类型错误");
        }
        return value;
    }

    /**
     * 获取int
     */
    public static int getInt(Map<String, Object> map, String key) {
        String value = "";
        int result = 0;
        try {
            value = map.get(key) == null ? "" : map.get(key).toString();
            result = Integer.parseInt(value);
        } catch (Exception e) {
            throw new ParamException("类型错误");
        }
        return result;
    }

    /**
     * 获取Long
     */
    public static Long getLong(Map<String, Object> map, String key) {
        String value = "";
        long result = 0l;
        try {
            value = map.get(key) == null ? "" : map.get(key).toString();
            result = Long.parseLong(value);
        } catch (Exception e) {
            throw new ParamException("类型错误");
        }
        return result;
    }

    /**
     * 获取int,空字符串=0
     */
    public static int getIntFromEmptyStr(Map<String, Object> map, String key) {
        int result = 0;
        try {
            result = getString(map, key) == "" ? 0 : getInt(map, key);
        } catch (Exception e) {
            throw new ParamException("类型错误");
        }
        return result;
    }

    /**
     * 获取int
     */
    public static double getDouble(Map<String, Object> map, String key) {
        String value = "";
        double result = 0;
        try {
            value = map.get(key) == null ? "" : map.get(key).toString();
            if (!value.equals("")) {
                result = Double.parseDouble(value);
            }
        } catch (Exception e) {
            throw new ParamException("类型错误");
        }
        return result;
    }

    /**
     * 转换map的KEY为小写
     */
    public static Map<String, Object> mapKey2Lower(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object value = map.get(key);
            result.put(key.toLowerCase(), value);
        }
        return result;
    }

    /**
     * 转换map的KEY为大写
     */
    public static Map<String, Object> mapKey2Upper(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object value = map.get(key);
            result.put(key.toUpperCase(), value);
        }
        return result;
    }

}
