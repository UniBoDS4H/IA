package com.ds4h.model.util.logger;

import com.drew.lang.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LoggerFactory {
    public static final String DEBUG_PROPERTY = "com.ds4h.model.util.logger.debug";
    private static final Map<String, Logger> myLoggersByClassName = new HashMap<>();

    private LoggerFactory() {}

    public static Logger getImageJLogger(@NotNull final String className) {
        if (myLoggersByClassName.containsKey(className)) {
            return myLoggersByClassName.get(className);
        }
        final Logger logger = new ImageJLogger(className);
        myLoggersByClassName.put(className, logger);
        return logger;
    }
}
