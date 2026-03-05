package com.ds4h.model.util.logger;

import com.drew.lang.annotations.NotNull;
import ij.IJ;

public class ImageJLogger implements Logger {
    @NotNull private final static String ERROR = "ERROR", WARNING = "WARNING", INFO = "INFO";
    @NotNull private final String myClassName;

    public ImageJLogger(@NotNull final String className) {
        myClassName = className;
    }

    @Override
    public void log(String message) {
        if (LoggerManager.isLoggingEnabled()) {
            IJ.log(formatMessage(INFO, message));
        }
    }

    @Override
    public void logError(String message) {
        if (LoggerManager.isLoggingEnabled()) {
            IJ.log(formatMessage(ERROR, message));
        }
    }

    @Override
    public void logWarning(String message) {
        if (LoggerManager.isLoggingEnabled()) {
            IJ.log(formatMessage(WARNING, message));
        }
    }

    @NotNull
    private String formatMessage(@NotNull final String logType, @NotNull final String message) {
        return String.format("[ %s ] log type(%s) -> current message: %s", myClassName, logType, message);
    }
}
