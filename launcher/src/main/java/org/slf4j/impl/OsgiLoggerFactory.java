package org.slf4j.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Tumin on 2016-08-15
 */
public class OsgiLoggerFactory implements ILoggerFactory {
    private final Map<String, OsgiLogger> loggers = new HashMap<>();
    private static LogService service;
    private static ServiceReference<LogService> ref;

    public static void setContext(BundleContext context) {
        if (ref != null) {
            ref.getBundle().getBundleContext().ungetService(ref);
        }
        ref = context.getServiceReference(LogService.class);
        service = context.getService(ref);
    }

    @Override
    public Logger getLogger(String s) {
        synchronized (loggers) {
            if (!loggers.containsKey(s)) {
                loggers.put(s, new OsgiLogger(service));
            }
            return loggers.get(s);
        }
    }
}
