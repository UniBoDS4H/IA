package com.ds4h.model.util.logger;

import com.drew.lang.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LoggerFactory {
    private static final Map<String, Logger> myLoggersByClassName = new HashMap<>();

    private LoggerFactory() {}

    @NotNull
    public static Logger getImageJLogger(@NotNull final Class<?> clazz) {
        return getImageJLogger(clazz.getName());
    }

    @NotNull
    public static Logger getImageJLogger(@NotNull final String className) {
        if (myLoggersByClassName.containsKey(className)) {
            return myLoggersByClassName.get(className);
        }
        final Logger logger = new ImageJLogger(className);
        myLoggersByClassName.put(className, logger);
        return logger;
    }
}
