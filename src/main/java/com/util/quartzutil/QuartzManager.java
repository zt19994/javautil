package com.util.quartzutil;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * quartz 定时任务管理
 *
 * @author zt1994 2019/9/10 18:07
 */
public class QuartzManager {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    /**
     * 添加一个定时任务
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @param jobClass         任务
     * @param cron             时间设置
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                              Class jobClass, String cron) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 添加一个定时任务
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @param jobClass         任务
     * @param seconds          秒
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                              Class jobClass, int seconds) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(
                    SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(seconds)
                    .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)
            );
            // 创建Trigger对象
            SimpleTrigger trigger = (SimpleTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加一个定时任务
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @param jobClass
     * @param cron
     * @param coSpaceId
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                              Class jobClass, String cron, String coSpaceId) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("coSpaceId", coSpaceId);
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)
                    .usingJobData(jobDataMap)
                    .build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 添加一个定时任务
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @param jobClass         任务
     * @param cron             时间设置
     * @param coSpaceId        coSpaceId
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                              Class jobClass, String cron, String coSpaceId, String confNum) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("coSpaceId", coSpaceId);
            jobDataMap.put("confNum", confNum);
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)
                    .usingJobData(jobDataMap)
                    .build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 添加一个定时任务
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @param jobClass         任务
     * @param seconds           秒
     * @param coSpaceId        coSpaceId
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                              Class jobClass, int seconds, String coSpaceId, String confNum) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("coSpaceId", coSpaceId);
            jobDataMap.put("confNum", confNum);
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)
                    .usingJobData(jobDataMap)
                    .build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            // 触发器时间设定
            triggerBuilder.withSchedule(
                    SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(seconds)
                            .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)
            );
            // 创建Trigger对象
            SimpleTrigger trigger = (SimpleTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 修改一个任务的触发时间
     *
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @param cron             时间设置
     */
    public static void modifyJobTime(String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 修改一个任务的触发时间
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @param cron             时间设置
     */
    public static void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                                     String cron) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 先删除，然后在创建一个新的Job
                JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
                Class<? extends Job> jobClass = jobDetail.getJobClass();
                removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 修改一个任务的触发时间
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @param cron             时间设置
     * @param coSpaceId        coSpaceId
     */
    public static void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                                     Class jobClass, String cron, String coSpaceId, String confNum) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron, coSpaceId, confNum);
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 先删除，然后在创建一个新的Job
                JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
                jobClass = jobDetail.getJobClass();
                removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron, coSpaceId, confNum);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 修改一个任务的触发时间
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @param seconds           秒
     * @param coSpaceId        coSpaceId
     */
    public static void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                                     Class jobClass, int seconds, String coSpaceId, String confNum) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, seconds, coSpaceId, confNum);
                return;
            }

            // 先删除，然后在创建一个新的Job
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
            jobClass = jobDetail.getJobClass();
            removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
            addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, seconds, coSpaceId, confNum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 移除一个任务
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     */
    public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 停止一个任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    public static void pauseJob(String jobName, String jobGroupName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            // 暂停任务
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 恢复一个任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    public static void resumeJob(String jobName, String jobGroupName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            // 恢复任务
            scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 暂停调度中所有的job任务
     *
     * @throws SchedulerException
     */
    public void pauseAll() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.pauseAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 恢复调度中所有的job的任务
     *
     * @throws SchedulerException
     */
    public void resumeAll() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.resumeAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
