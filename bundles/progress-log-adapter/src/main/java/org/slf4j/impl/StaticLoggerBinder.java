package org.slf4j.impl;

import org.eientei.progress.bundles.progress.log.adapter.LogServiceProvider;
import org.eientei.progress.bundles.progress.log.adapter.OsgiLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * Created by Alexander Tumin on 2016-08-15
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {
    private static StaticLoggerBinder singletone;
    private static LogServiceProvider provider = new LogServiceProvider();

    public static StaticLoggerBinder getSingleton() {
        if (singletone == null) {
            singletone = new StaticLoggerBinder();

        }
        return singletone;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return new OsgiLoggerFactory(provider);
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return OsgiLoggerFactory.class.getName();
    }

    public static LogServiceProvider getProvider() {
        return provider;
    }
}
