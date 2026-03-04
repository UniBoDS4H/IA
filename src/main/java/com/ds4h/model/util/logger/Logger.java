package com.ds4h.model.util.logger;

import com.drew.lang.annotations.NotNull;

public interface Logger {
    void log(@NotNull final String message);
    void logError(@NotNull final String message);
    void logWarning(@NotNull final String message);
}
