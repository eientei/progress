package org.slf4j.impl;

import org.osgi.service.log.LogService;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

/**
 * Created by Alexander Tumin on 2016-08-15
 */
public class OsgiLogger extends MarkerIgnoringBase {
    private LogService logService;

    public OsgiLogger(LogService logService) {
        this.logService = logService;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String s) {

    }

    @Override
    public void trace(String s, Object o) {

    }

    @Override
    public void trace(String s, Object o, Object o1) {

    }

    @Override
    public void trace(String s, Object... objects) {

    }

    @Override
    public void trace(String s, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(String s) {
        logService.log(LogService.LOG_DEBUG, s);
    }

    @Override
    public void debug(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        logService.log(LogService.LOG_DEBUG, ft.getMessage());
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        logService.log(LogService.LOG_DEBUG, ft.getMessage());
    }

    @Override
    public void debug(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        logService.log(LogService.LOG_DEBUG, ft.getMessage());
    }

    @Override
    public void debug(String s, Throwable throwable) {
        logService.log(LogService.LOG_DEBUG, s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(String s) {
        logService.log(LogService.LOG_INFO, s);
    }

    @Override
    public void info(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        logService.log(LogService.LOG_INFO, ft.getMessage());
    }

    @Override
    public void info(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        logService.log(LogService.LOG_INFO, ft.getMessage());
    }

    @Override
    public void info(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        logService.log(LogService.LOG_INFO, ft.getMessage());
    }

    @Override
    public void info(String s, Throwable throwable) {
        logService.log(LogService.LOG_INFO, s, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String s) {
        logService.log(LogService.LOG_WARNING, s);
    }

    @Override
    public void warn(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        logService.log(LogService.LOG_WARNING, ft.getMessage());
    }

    @Override
    public void warn(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        logService.log(LogService.LOG_WARNING, ft.getMessage());
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        logService.log(LogService.LOG_WARNING, ft.getMessage());
    }

    @Override
    public void warn(String s, Throwable throwable) {
        logService.log(LogService.LOG_WARNING, s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(String s) {
        logService.log(LogService.LOG_ERROR, s);
    }

    @Override
    public void error(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        logService.log(LogService.LOG_ERROR, ft.getMessage());
    }

    @Override
    public void error(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        logService.log(LogService.LOG_ERROR, ft.getMessage());
    }

    @Override
    public void error(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        logService.log(LogService.LOG_ERROR, ft.getMessage());
    }

    @Override
    public void error(String s, Throwable throwable) {
        logService.log(LogService.LOG_ERROR, s, throwable);
    }
}
