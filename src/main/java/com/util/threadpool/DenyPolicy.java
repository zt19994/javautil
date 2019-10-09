package com.util.threadpool;

/**
 * 任务拒绝策略
 *
 * @author zt1994 2019/10/9 21:20
 */
@FunctionalInterface
public interface DenyPolicy {

    /**
     * 拒绝方法
     *
     * @param runnable
     * @param threadPool
     */
    void reject(Runnable runnable, ThreadPool threadPool);

    /**
     * 该拒绝策略会向直接将任务丢弃
     */
    class DiscardDenyPoliy implements DenyPolicy {

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            // do nothing
        }
    }


    /**
     * 该拒绝策略会向任务提交者抛出异常
     */
    class AbortDenyPoliy implements DenyPolicy {

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            throw new RunnableDenyException("The runnable " + runnable + " will be abort.");
        }
    }


    /**
     * 该拒绝策略会使任务在提交者所在的线程中执行任务
     */
    class RunnerDenyPoliy implements DenyPolicy {

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            if (!threadPool.isShutdown()) {
                runnable.run();
            }
        }
    }
}
