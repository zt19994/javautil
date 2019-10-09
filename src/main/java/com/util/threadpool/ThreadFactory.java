package com.util.threadpool;

/**
 * 创建线程的工厂
 *
 * @author zt1994 2019/10/9 21:16
 */
@FunctionalInterface
public interface ThreadFactory {

    /**
     * 创建线程
     *
     * @param runnable
     * @return
     */
    Thread createThread(Runnable runnable);
}
