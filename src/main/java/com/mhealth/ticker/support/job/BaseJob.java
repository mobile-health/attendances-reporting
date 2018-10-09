package com.mhealth.ticker.support.job;

import com.mhealth.ticker.support.data.Config;
import com.mhealth.ticker.support.data.HealthCheck;
import com.mhealth.ticker.support.data.mongo.MongoHelper;
import com.mhealth.ticker.support.service.SlackHookService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseJob implements Job {

    private static final Map<String, Long> LAST_ERROR_SEND_TIME = new HashMap<>();

    private static final long ERROR_SEND_RATE = 30 * 1000;

    public static final List<Class<? extends BaseJob>> REGISTERED_JOBS = new ArrayList<>();

    static {
        REGISTERED_JOBS.add(AttendanceJob.class);
        REGISTERED_JOBS.add(HealthCheckJob.class);
        LAST_ERROR_SEND_TIME.put(AttendanceJob.class.getSimpleName(), 0L);
    }

    public static final Logger logger = Logger.getLogger(BaseJob.class.getName());

    protected String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long start = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        //logger.info("Start " + getName() + " session " + uuid);
        SlackHookService slackHookService = SlackHookService.getInstance();
        HealthCheck healthCheck = new HealthCheck();
        try {
            run(slackHookService, uuid);
            healthCheck.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            healthCheck.setMessage(e.getMessage());
            healthCheck.setStatus(false);
            long lastSendTime = LAST_ERROR_SEND_TIME.get(getName());
            if (lastSendTime == 0 || System.currentTimeMillis() - lastSendTime > ERROR_SEND_RATE) {

                hookError(slackHookService, "Could not complete " + getName() + " session " + uuid, e);
                LAST_ERROR_SEND_TIME.replace(getName(), System.currentTimeMillis());
            }
        }
        long end = System.currentTimeMillis();
        //logger.info("Finish " + getName() + "  cronjob. Execution time: " + (end - start) + "ms. Session: " + uuid);
        try {
            healthCheck.setName(getName());
            healthCheck.setEndTime(new Date(end));
            healthCheck.setStartTime(new Date(start));
            healthCheck.setExecutionTime(BigDecimal.valueOf(end - start));
            healthCheck.setSession(uuid);
            MongoHelper.insert(healthCheck, HealthCheck.class);
            //logger.info("Saved session " + uuid + " job name " + getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract void run(SlackHookService slackHookService,
                      String session) throws Exception;

    public void register() throws SchedulerException {
        logger.info("Register " + getName() + " cron");
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        JobDetail job = JobBuilder.newJob(this.getClass()).build();
        scheduler.scheduleJob(job, TriggerBuilder.newTrigger()
                .withIdentity(getName(), "job")
                .withSchedule(scheduleBuilder())
                .build());
    }

    abstract ScheduleBuilder scheduleBuilder();

    public String getTag() {
        return "[" + getName() + "]";
    }

    public void log(String log) {
    	System.out.println(log);
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable e) {
        if (e != null) {
            logger.log(Level.SEVERE, message, e);
        } else {
            logger.log(Level.SEVERE, message);
        }
    }

    public void hookError(SlackHookService slackHookService, String error) {
        hookError(slackHookService, error, null);
    }

    public void hookError(SlackHookService slackHookService, String error, Throwable e) {
        try {
            slackHookService.hookError(error, e);
        } catch (Exception ex) {

        }
    }
}
