package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * Created by Alexander Tumin on 2016-08-15
 */
public class OsgiFactoryBinder implements LoggerFactoryBinder {
    @Override
    public ILoggerFactory getLoggerFactory() {
        return new OsgiLoggerFactory();
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return OsgiLoggerFactory.class.getName();
    }
}
