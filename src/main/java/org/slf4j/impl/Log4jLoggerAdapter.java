/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.impl;

import java.io.Serializable;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.spi.LocationAwareLogger;

public final class Log4jLoggerAdapter
extends MarkerIgnoringBase
implements Serializable,
LocationAwareLogger {
    final transient Logger logger;
    static final String FQCN = Log4jLoggerAdapter.class.getName();
    final boolean traceCapable;

    Log4jLoggerAdapter(Logger logger) {
        this.logger = logger;
        this.name = logger.getName();
        this.traceCapable = this.isTraceCapable();
    }

    private boolean isTraceCapable() {
        try {
            this.logger.isTraceEnabled();
            return true;
        }
        catch (NoSuchMethodError noSuchMethodError) {
            return false;
        }
    }

    public void debug(String string) {
        this.logger.log(FQCN, Level.DEBUG, string, null);
    }

    public void info(String string) {
        this.logger.log(FQCN, Level.INFO, string, null);
    }

    public void warn(String string) {
        this.logger.log(FQCN, Level.WARN, string, null);
    }

    public void warn(String string, Throwable throwable) {
        this.logger.log(FQCN, Level.WARN, string, throwable);
    }

    public void error(String string) {
        this.logger.log(FQCN, Level.ERROR, string, null);
    }

    public void error(String string, Throwable throwable) {
        this.logger.log(FQCN, Level.ERROR, string, throwable);
    }
}

