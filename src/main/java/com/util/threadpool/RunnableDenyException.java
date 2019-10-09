package com.util.threadpool;

/**
 * 通知任务提交者，任务队列已无法再接收新的任务
 *
 * @author zt1994 2019/10/9 21:29
 */
public class RunnableDenyException extends RuntimeException {

    public RunnableDenyException(String message) {
        super(message);
    }
}
