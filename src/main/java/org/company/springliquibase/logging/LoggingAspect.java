package org.company.springliquibase.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Pointcut to intercept all methods annotated with @Loggable
    @Pointcut("@annotation(org.company.springliquibase.logging.Loggable)")
    public void loggableMethods() {
    }

    @Before("loggableMethods() && @annotation(loggable)")
    public void logBeforeMethod(Loggable loggable) {
        long startTime = System.currentTimeMillis();
        LogContext.setStartTime(startTime);
        log.info("Action: {} - Method started", loggable.action());
    }

    @After("loggableMethods() && @annotation(loggable)")
    public void logAfterMethod(Loggable loggable) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - LogContext.getStartTime();
        log.info("Action: {} - Method completed successfully in {} ms", loggable.action(), duration);
    }

    @AfterThrowing(pointcut = "loggableMethods() && @annotation(loggable)", throwing = "exception")
    public void logAfterThrowingMethod(Loggable loggable, Throwable exception) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - LogContext.getStartTime();
        log.error("Action: {} - Method failed with exception: {}. Duration: {} ms",
                loggable.action(), exception.getMessage(), duration);
    }
}
