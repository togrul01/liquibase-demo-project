package org.company.springliquibase.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class LoggingUtil {
    private static final String ACTION_PATTERN = "ActionLog.{} | traceId: {} | {}";
    private static final String ERROR_PATTERN = "ErrorLog.{} | traceId: {} | error: {}";
    private static final String WARNING_PATTERN = "WarningLog.{} | traceId: {} | warning: {}";
    private static final String INFO_PATTERN = "InfoLog.{} | traceId: {} | info: {}";

    private static String getTraceId() {
        return MDC.get("traceId");
    }

    private static void logInfo(String action, String context, Object message) {
        if (log.isInfoEnabled()) {
            String traceId = getTraceId();
            String logMessage = String.format(INFO_PATTERN, action, traceId, context + ": " + message);
            log.info(logMessage);
        }
    }

    private static void logError(String action, String context, String message, Exception e) {
        if (log.isErrorEnabled()) {
            String traceId = getTraceId();
            String logMessage = String.format(ERROR_PATTERN, action, traceId, context + ": " + message);
            log.error(logMessage, e);
        }
    }

    private static void logWarning(String action, String context, String message) {
        if (log.isWarnEnabled()) {
            String traceId = getTraceId();
            String logMessage = String.format(WARNING_PATTERN, action, traceId, context + ": " + message);
            log.warn(logMessage);
        }
    }

    public static <T> void logActionStart(String action, T request) {
        String traceId = getTraceId();
        String logMessage = String.format(ACTION_PATTERN, action + ".start", traceId, request);
        log.info(logMessage);
    }

    public static <T> void logActionEnd(String action, T request) {
        String traceId = getTraceId();
        String logMessage = String.format(ACTION_PATTERN, action + ".end", traceId, request);
        log.info(logMessage);
    }

    public static <T> void logApiResponse(String action, T response) {
        String traceId = getTraceId();
        String logMessage = String.format(ACTION_PATTERN, action + ".response", traceId, response);
        log.info(logMessage);
    }

    public void logCardCreation(String action, String message) {
        logInfo(action, "card.creation", message);
    }

    public void logCardUpdate(String action, String message) {
        logInfo(action, "card.update", message);
    }

    public void logCardDeletion(String action, String message) {
        logInfo(action, "card.deletion", message);
    }

    public static void logCardList(String action, String message) {
        logInfo(action, "card.list", message);
    }

    public static void logCardBalance(String action, String message) {
        logInfo(action, "card.balance", message);
    }

    public static void logCardValidation(String action, String message) {
        logInfo(action, "card.validation", message);
    }

    public static void logCardPreparation(String action, String message) {
        logInfo(action, "card.preparation", message);
    }

    public static void logCardCvv(String action, String message) {
        logInfo(action, "card.cvv", message);
    }

    public static void logCardDates(String action, String message) {
        logInfo(action, "card.dates", message);
    }

    public static void logError(String action, Exception e, String message) {
        logError(action, "error", message, e);
    }

    public void logWarning(String action, String message) {
        logWarning(action, "warning", message);
    }

    public void logInfo(String action, String message) {
        logInfo(action, "info", message);
    }
}