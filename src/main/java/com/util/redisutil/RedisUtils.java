package com.util.redisutil;

import com.util.springutil.SpringContextUtil;

/**
 * @author zt1994 2020/3/27 16:27
 */
public class RedisUtils {

    /**
     * redis 工具类
     */
    private static RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("redisUtil");

    /**
     * redis获取缓存
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return redisUtil.get(key);
    }


    /**
     * redis添加缓存
     *
     * @param key
     * @param object
     * @return
     */
    public static boolean set(String key, Object object) {
        return redisUtil.set(key, object);
    }


    /**
     * 删除对应的value
     *
     * @param key
     */
    public static void remove(String key) {
        redisUtil.remove(key);
    }


    /**
     * 加锁
     *
     * @param key
     * @return
     */
    public static boolean lock(String key) {
        return redisUtil.lock(key);
    }


    /**
     * 释放锁
     *
     * @param key
     * @return
     */
    public static void unlock(String key) {
        redisUtil.delete(key);
    }

}
