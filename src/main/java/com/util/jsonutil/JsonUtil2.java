package com.util.jsonutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.util.traceutil.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * json 工具类
 *
 * @author zt1994 2019/8/19 10:37
 */
public class JsonUtil2 {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final SerializeConfig CONFIG;

    static {
        CONFIG = new SerializeConfig();
        // 使用和json-lib兼容的日期输出格式
        CONFIG.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        // 使用和json-lib兼容的日期输出格式
        CONFIG.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    private static final SerializerFeature[] FEATURES = {
            // 输出空置字段
            SerializerFeature.WriteMapNullValue,
            // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullListAsEmpty,
            // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullNumberAsZero,
            // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullBooleanAsFalse,
            // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.WriteNullStringAsEmpty
    };


    /**
     * 转换成字符串
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object, CONFIG);
    }


    /**
     * 转换成字符串 ,带有过滤器
     *
     * @param object
     * @return
     */
    public static String toJsonWithFeatures(Object object) {
        return JSON.toJSONString(object, CONFIG, FEATURES);
    }


    /**
     * 转成bean对象
     *
     * @param jsonData
     * @return
     */
    public static Object toBean(String jsonData) {
        return JSON.parse(jsonData);
    }


    /**
     * 转成具体的泛型bean对象
     *
     * @param jsonData
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(String jsonData, Class<T> clazz) {
        return JSON.parseObject(jsonData, clazz);
    }


    /**
     * 转成具体的泛型bean对象
     *
     * @param jsonData
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T toBean(String jsonData, Type type) {
        return JSON.parseObject(jsonData, type);
    }


    /**
     * 转换为数组Array
     *
     * @param text
     * @param <T>
     * @return
     */
    public static <T> Object[] toArray(String text) {
        return JSON.parseArray(text, (Class<Object>) null).toArray();
    }


    /**
     * 转换为具体的泛型数组Array
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }


    /**
     * 转换为具体的泛型List
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }


    /**
     * 将javabean转化为序列化的json字符串
     *
     * @param keyValue
     * @return
     */
    public static Object beanToJson(KeyValue keyValue) {
        String textJson = JSON.toJSONString(keyValue);
        return JSON.parse(textJson);
    }


    /**
     * 将string转化为序列化的json字符串
     *
     * @param text
     * @return
     */
    public static Object textToJson(String text) {
        return JSON.parse(text);
    }


    /**
     * json字符串转化为map
     *
     * @param s
     * @return
     */
    public static Map stringToMap(String s) {
        return JSONObject.parseObject(s);
    }


    /**
     * object 转 map
     *
     * @param object
     * @return
     */
    public static Map objectToMap(Object object) {
        String jsonString = toJsonString(object);
        return stringToMap(jsonString);
    }


    /**
     * 将map转化为string
     *
     * @param map
     * @return
     */
    public static String mapToString(Map map) {
        return JSONObject.toJSONString(map);
    }


    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> getListMap(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
        return list;
    }

}
