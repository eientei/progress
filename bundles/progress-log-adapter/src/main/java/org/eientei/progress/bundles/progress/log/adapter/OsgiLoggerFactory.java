package org.eientei.progress.bundles.progress.log.adapter;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.impl.SimpleLogger;
import org.slf4j.impl.SimpleLoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Tumin on 2016-08-15
 */
public class OsgiLoggerFactory implements ILoggerFactory {
    static {
        if (System.getProperty(SimpleLogger.SHOW_THREAD_NAME_KEY) == null) {
            System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "false");
        }
        if (System.getProperty(SimpleLogger.SHOW_DATE_TIME_KEY) == null) {
            System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
        }
    }

    private static SimpleLoggerFactory factory = new SimpleLoggerFactory();


    private final Map<String, Logger> loggers = new HashMap<>();
    private LogServiceProvider provider;

    public OsgiLoggerFactory(LogServiceProvider provider) {
        this.provider = provider;
    }

    @Override
    public Logger getLogger(String s) {
        synchronized (loggers) {
            if (!loggers.containsKey(s)) {
                loggers.put(s, new OsgiLogger(Thread.currentThread().getName(), s, provider));
            }
            return loggers.get(s);
        }
    }
}
