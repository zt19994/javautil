package com.util.threadpool;

/**
 * 用于线程池内部，不断从queue中取出某个runnable
 *
 * @author zt1994 2019/10/9 21:32
 */
public class InternalTask implements Runnable {

    private final RunableQueue runableQueue;

    private volatile boolean running = true;

    public InternalTask(RunableQueue runableQueue) {
        this.runableQueue = runableQueue;
    }

    @Override
    public void run() {

        // 如果当前任务为running并且没有被中断，则其将不断从queue中获取runnable，然后执行run方法
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Runnable task = runableQueue.take();
                task.run();
            } catch (Exception e) {
                running = false;
                break;
            }
        }
    }


    /**
     * 停止当前任务，主要会在线程池的shutdown方法中使用
     */
    public void stop() {
        this.running = false;
    }
}
