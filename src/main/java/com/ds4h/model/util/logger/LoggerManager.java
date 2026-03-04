package com.ds4h.model.util.logger;

public class LoggerManager {
    private static final String DEBUG_PROPERTY = "com.ds4h.model.util.logger.debug";
    private LoggerManager() {}

    public static void enableLogging() {
        System.setProperty(DEBUG_PROPERTY, Boolean.TRUE.toString());
    }

    public static void disableLogging() {
        System.setProperty(DEBUG_PROPERTY, Boolean.FALSE.toString());
    }

    public static boolean isLoggingEnabled() {
        return Boolean.parseBoolean(System.getProperty(DEBUG_PROPERTY));
    }
}
