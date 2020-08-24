package com.util.beanutil;

import com.util.traceutil.StackTraceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Bean 转换工具类
 *
 * @author zt1994 2019/8/19 18:12
 */
public class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);


    /**
     * 将bean转化为参数 name1=value1&name2=value2
     *
     * @param bean bean
     * @return
     */
    public static String beanToParam(Object bean) {
        StringBuilder nv = new StringBuilder();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
        if (beanInfo != null) {
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!StringUtils.equals(propertyName, "class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = null;
                    try {
                        result = readMethod.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
                    }
                    if (result != null) {
                        nv.append(propertyName).append("=").append(result.toString()).append("&");
                    }
                }
            }
        }
        if (nv.length() > 0) {
            nv.delete(nv.length() - 1, nv.length());
        }
        return nv.toString();
    }

    /**
     * 以第一个实体类为主，如果第一个的实体类某个字段为空，则会吧第二个实体类的值取过来进行赋值，如果不为空的则不作改变
     * @author: LYP
     * @date: 2020年07月15日 上午11:53:19
     * @param sourceBean
     * @param targetBean
     * @return
     */
    private static Object combineSydwCore(Object sourceBean, Object targetBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();
        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            if (Modifier.isStatic(sourceField.getModifiers())) {
                continue;
            }
            Field targetField = targetFields[i];
            if (Modifier.isStatic(targetField.getModifiers())) {
                continue;
            }
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (!(sourceField.get(sourceBean) == null) && !"serialVersionUID".equals(sourceField.getName().toString())) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error("POJO合并出错："+e);
            }
        }
        return targetBean;
    }
}


