package com.frauddetectservice.logging;

public interface LoggingService {
    void log(String message);
    void info(String message);
    void debug(String message);
    void warn(String message);
    void error(String message, Throwable throwable);
}
