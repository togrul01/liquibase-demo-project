package org.company.springliquibase.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingUtil {

    // Generic log method for API responses
    public static <T> void logApiResponse(String action, T response) {
        log.info("ActionLog.{}.success response: {}", action, response);
    }
}
