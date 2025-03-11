package org.company.springliquibase.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

@UtilityClass
public class LocalizationUtil {

    public static String getLocalizedMessageByStatusCode(String bundleName, Integer statusCode) {
        var resourceBundle = ResourceBundle.getBundle(bundleName, LocaleContextHolder.getLocale());
        return resourceBundle.getString(getMessageKeyByStatusCode(statusCode));
    }

    public static String getLocalizedMessageByKey(String bundleName, String key) {
        var locale = LocaleContextHolder.getLocale();
        return ResourceBundle.getBundle(bundleName, locale).getString(key);
    }

    private static String getMessageKeyByStatusCode(Integer statusCode) {
        return switch (statusCode) {
            case 400 -> "client.error.exception";           // Bad Request
            case 401 -> "unauthorized.exception";           // Unauthorized
            case 403 -> "forbidden.exception";              // Forbidden
            case 404 -> "user.not.found.exception";         // Not Found
            case 409 -> "user.already.exist.exception";     // Conflict (Duplicate entry)
            case 422 -> "unprocessable.entity.exception";   // Unprocessable Entity
            case 500 -> "server.error.exception";           // Internal Server Error
            case 502 -> "bad.gateway.exception";            // Bad Gateway
            case 503 -> "service.unavailable.exception";    // Service Unavailable
            case 504 -> "gateway.timeout.exception";        // Gateway Timeout
            default -> "unexpected.exception";              // Unknown or unspecified error
        };
    }

}