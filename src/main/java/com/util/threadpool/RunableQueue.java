package com.util.threadpool;

/**
 * 任务队列，主要用于缓存提交到线程池中的任务
 *
 * @author zt1994 2019/10/9 21:13
 */
public interface RunableQueue {

    /**
     * 当有新的任务进来时首先会offer到队列中
     *
     * @param runnable
     */
    void offer(Runnable runnable);

    /**
     * 工作线程通过take方法获取Runable
     *
     * @return
     */
    Runnable take();

    /**
     * 获取任务队列中任务的数量
     *
     * @return
     */
    int size();
}
