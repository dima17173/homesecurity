package com.impl.homesecurity.job.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dima.
 * Creation date 20.12.18.
 */
@Aspect
@Configuration
public class PerformanceAspectConfig {

    @Value("${controllers.performance-logging.ms}")
    private long serviceEvaluatedTime;

    @Value("${jobs.performance-logging.ms}")
    private long jobEvaluatedTime;

    private final Logger logJob = LoggerFactory.getLogger("Scheduled");
    private final Logger logService = LoggerFactory.getLogger(PerformanceAspectConfig.class);

    @Pointcut("execution(* com.impl.homesecurity.job.service.*.*(..))")
    public void jobs() {
    }

    @Pointcut("execution(* com.impl.homesecurity.service.impl.*.*(..))")
    public void controllers() {
    }

    @Around("jobs()")
    public Object processJobs(ProceedingJoinPoint pjp) throws Throwable {
        return getObject(pjp, jobEvaluatedTime, logJob);
    }

    @Around("controllers()")
    public Object processControllers(ProceedingJoinPoint pjp) throws Throwable {
        return getObject(pjp, serviceEvaluatedTime, logService);
    }

    private Object getObject(ProceedingJoinPoint pjp, long jobEvaluatedTime, Logger log) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = pjp.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;

        if (timeTaken > jobEvaluatedTime) {
            log.warn("Time Taken by {} is {}", pjp, timeTaken);
        }
        return proceed;
    }
}
