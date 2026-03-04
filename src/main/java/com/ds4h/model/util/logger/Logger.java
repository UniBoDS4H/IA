package com.ds4h.model.util.logger;

import com.drew.lang.annotations.NotNull;

public interface Logger {
    /**
     * Log an information message.
     * @param message input message to log.
     */
    void log(@NotNull final String message);

    /**
     * Log an error message.
     * @param message input error message to log.
     */
    void logError(@NotNull final String message);

    /**
     * Log a warning message.
     * @param message input warning message to log.
     */
    void logWarning(@NotNull final String message);
}
