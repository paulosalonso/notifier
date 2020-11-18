package com.github.paulosalonso.notifier;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

public abstract class LoggerHelper {
    private LoggerHelper() {}

    public static ListAppender<ILoggingEvent> getListAppender(Class<?> forClass) {
        Logger logger = (Logger) LoggerFactory.getLogger(forClass);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        return appender;
    }
}
