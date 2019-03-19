package com.util.jsonutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.util.logutil.MyLogger;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import java.util.*;

/**
 * JSON工具类
 *
 * @author zhongtao on 2018/12/29
 */
public class JsonUtil {

    /**
     * 日志
     */
    public static final MyLogger LOGGER = new MyLogger(JsonUtil.class);

    /**
     * 构造
     */
    private JsonUtil() {
    }

    /**
     * 从json HASH表达式中获取一个map，该map支持嵌套功能 形如：{"id" : "johncon", "name" : "小强"}
     * 注意commons-collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap
     *
     * @param jsonString
     * @return Map
     */
    public static Map<String, Object> getMapFromJson(String jsonString) {
        setDataFormat2JAVA();
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Map<String, Object> map = new HashMap<String, Object>();
        for (Iterator<?> iter = jsonObject.keys(); iter.hasNext(); ) {
            String key = (String) iter.next();
            map.put(key, jsonObject.get(key));
        }
        return map;
    }

    /**
     * 将json类型的数组换为Object类型数组
     *
     * @param jsonString json串
     * @param clazz
     * @return Object[]
     */
    public static Object[] getDTOArray(String jsonString, Class<?> clazz) {
        // 将jsonString串转为JSONArray赋值给JSONArray类型的array
        JSONArray array = JSONArray.fromObject(jsonString);
        // 定义Object类型数组
        Object[] obj = new Object[array.size()];
        // 遍历array
        for (int i = 0; i < array.size(); i++) {
            // 获取array[i]的json对象
            JSONObject jsonObject = array.getJSONObject(i);
            // 并赋值给obj[i]
            obj[i] = JSONObject.toBean(jsonObject, clazz);
        }
        // 返回obj
        return obj;
    }

    /**
     * 从一个JSON 对象字符格式中得到一个java对象，形如： {"id" : idValue, "name" : nameValue,
     * "aBean" : {"aBeanId" : aBeanIdValue, ...}}
     *
     * @param jsonString
     * @param clazz
     * @return Object
     */
    public static Object getDTO(String jsonString, Class<?> clazz) {
        JSONObject jsonObject = null;
        try {
            setDataFormat2JAVA();
            jsonObject = JSONObject.fromObject(jsonString);
        } catch (Exception e) {
            LOGGER.error("JSON转换对象失败！", e);
        }
        return JSONObject.toBean(jsonObject, clazz);
    }

    /**
     * 设定日期转换格式
     */
    private static void setDataFormat2JAVA() {
        // 设定日期转换格式
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd HH:mm:ss"}));
    }

    /**
     * 把数据对象转换成json字符串 DTO对象形如：{"id" : idValue, "name" : nameValue, ...}
     * 数组对象形如：[{}, {}, {}, ...] map对象形如：{key1 : {"id" : idValue, "name" : nameValue, ...}, key2 : {}, ...}
     *
     * @param object
     * @return String
     */
    public static String getJSONString(Object object) {
        SerializeConfig mapping = new SerializeConfig();
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

        return JSON.toJSONString(object, mapping, new SerializeFilter[0]);
    }


    /**
     * json 转 list
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> json2List(Object json) {
        com.alibaba.fastjson.JSONArray jsonArr = (com.alibaba.fastjson.JSONArray) json;
        List<Map<String, Object>> arrList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < jsonArr.size(); ++i) {
            arrList.add(strJson2Map(jsonArr.getString(i)));
        }
        return arrList;
    }


    /**
     * json字符转map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> strJson2Map(String json) {
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(json);
        return json2Map(jsonObject);
    }


    /**
     * json转map
     *
     * @param jsonObject
     * @return
     */
    public static Map<String, Object> json2Map(com.alibaba.fastjson.JSONObject jsonObject) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> param = (Map.Entry<String, Object>) it.next();
            if (param.getValue() instanceof com.alibaba.fastjson.JSONObject) {
                resMap.put(param.getKey(), strJson2Map(param.getValue().toString()));
            } else if (param.getValue() instanceof com.alibaba.fastjson.JSONArray) {
                resMap.put(param.getKey(), json2List(param.getValue()));
            } else {
                resMap.put(param.getKey(), com.alibaba.fastjson.JSONObject.toJSONString(param.getValue(), SerializerFeature.WriteClassName));
            }
        }
        return resMap;
    }
}
