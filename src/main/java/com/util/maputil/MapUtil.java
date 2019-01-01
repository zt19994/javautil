package com.util.maputil;

import com.util.logutil.MyLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据实体bean生成Map
 *
 * @author zhongtao on 2019/1/1
 */
public class MapUtil {

    /**
     * 日志
     */
    public static final MyLogger LOGGER = new MyLogger(MapUtil.class);

    private MapUtil() {
    }

    /**
     * 通过bean产生map对象：如map.put("username",username);map.put("password",password), key 值为bean的属性名 value 为bean的属性值
     *
     * @param bean 实体bean
     * @return Map
     */
    public static Map<String, Object> getParamsMap(Object bean) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 获取对象的class 包括父类class
        for (Class<?> clazz = bean.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                // 判断是否是静态的
                if (!Modifier.isStatic(fields[i].getModifiers())) {
                    String fieldName = fields[i].getName();
                    String getterMethodName = "get"
                            + Character.toUpperCase(fieldName.charAt(0))
                            + fieldName.substring(1, fieldName.length());
                    try {
                        Method method = clazz.getMethod(getterMethodName);
                        if (method != null) {
                            map.put(fieldName, method.invoke(bean));
                        }
                    } catch (IllegalAccessException e) {
                        LOGGER.error("", e);
                        map = null;
                    } catch (IllegalArgumentException e) {
                        LOGGER.error("", e);
                        map = null;
                    } catch (InvocationTargetException e) {
                        LOGGER.error("", e);
                        map = null;
                    } catch (NoSuchMethodException e) {
                        LOGGER.error("", e);
                        map = null;
                    } catch (SecurityException e) {
                        LOGGER.error("", e);
                        map = null;
                    }
                }
            }
        }
        return map;
    }
}
