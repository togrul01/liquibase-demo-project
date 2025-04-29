package org.company.springliquibase.logging;

public class LogContext {
    private LogContext() {
    }

    private static final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    public static void setStartTime(long startTime) {
        startTimeThreadLocal.set(startTime);
    }

    public static long getStartTime() {
        return startTimeThreadLocal.get();
    }

    public static void clear() {
        startTimeThreadLocal.remove();
    }
}
