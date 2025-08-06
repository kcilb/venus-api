package com.neptunesoftware.venusApis.Util;


import com.neptunesoftware.venusApis.Repository.CoreDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

public final class Logging {
    private Logging() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(Object object) {
        if (object == null) {
            return LoggerFactory.getLogger(Logging.class);
        }
        return getLogger(object.getClass());
    }

    public static void trace(String message, Object... args) {
        getLogger(getCallerClass()).trace(message, args);
    }

    public static void debug(String message, Object... args) {
        getLogger(getCallerClass()).debug(message, args);
    }

    public static void info(String message, Object... args) {
        getLogger(getCallerClass()).info(message, args);
    }

    public static void warn(String message, Object... args) {
        getLogger(getCallerClass()).warn(message, args);
    }

    public static void error(String message, Object... args) {
        getLogger(getCallerClass()).error(message, args);
    }

    public static void error(Throwable throwable) {
        getLogger(getCallerClass()).error(throwable.getMessage(), throwable);
    }

    public static void error(String message, Throwable throwable) {
        getLogger(getCallerClass()).error(message, throwable);
    }

    // MDC (Mapped Diagnostic Context) support
    public static void putToContext(String key, String value) {
        MDC.put(key, value);
    }

    public static void removeFromContext(String key) {
        MDC.remove(key);
    }

    public static void clearContext() {
        MDC.clear();
    }

    public static Map<String, String> getContext() {
        return MDC.getCopyOfContextMap();
    }

    private static Class<?> getCallerClass() {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
        } catch (ClassNotFoundException e) {
            return Logging.class;
        }
    }
}
