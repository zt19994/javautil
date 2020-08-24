package com.util.beanutil;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 参数校验工具类
 *
 * @author zt1994 2019/7/12 15:39
 */
public class BeanValidator {

    private static final Logger logger = LoggerFactory.getLogger(BeanValidator.class);

    /**
     * validator 工厂
     */
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();


    /**
     * 校验方法
     *
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    private static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }


    /**
     * 检验列表
     *
     * @param collection
     * @return
     */
    private static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object);
        } while (errors.isEmpty());
        return errors;
    }


    /**
     * 校验对象
     *
     * @param first
     * @param objects
     * @return
     */
    private static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first);
        }
    }


    /**
     * 把需要检测的bean引用该方法即可
     *
     * @param param VO
     * @throws ParamException
     */
    public static void check(Object param) throws ParamException {
        Map<String, String> map = BeanValidator.validateObject(param);
        // 如果并不为空说明有错误信息
        if (MapUtils.isNotEmpty(map)) {
            // 异常处理
            throw new ParamException(map.toString());
        }
    }


    /**
     * 参数是否异常
     *
     * @param param
     * @return true 不异常 false 异常
     * @throws ParamException
     */
    public static Boolean isValid(Object param) throws ParamException {
        Map<String, String> map = BeanValidator.validateObject(param);
        // 如果并不为空说明有错误信息
        if (MapUtils.isNotEmpty(map)) {
            // 异常处理
            logger.info("参数异常：{}", map.toString());
            return false;
        }
        return true;
    }
}

