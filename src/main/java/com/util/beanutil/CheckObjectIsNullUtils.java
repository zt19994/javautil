package com.util.beanutil;

import com.util.traceutil.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 校验对象字段是否都为空
 *
 * @author zt1994 2019/12/14 14:14
 */
public class CheckObjectIsNullUtils {

    private static final Logger logger = LoggerFactory.getLogger(CheckObjectIsNullUtils.class);


    /**
     * 判断对象是否为空，且对象的所有属性都为空
     * ps: boolean类型会有默认值false 判断结果不会为null 会影响判断结果
     * 序列化的默认值也会影响判断结果
     *
     * @param object
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean objCheckIsNull(Object object) {
        // 得到类对象
        Class clazz = (Class) object.getClass();
        // 得到所有属性
        Field[] fields = clazz.getDeclaredFields();
        // 定义返回结果，默认为true
        boolean flag = true;
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                // 得到属性值
                fieldValue = field.get(object);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
            }
            // 只要有一个属性值不为null 就返回false 表示对象不为null
            if (fieldValue != null) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
