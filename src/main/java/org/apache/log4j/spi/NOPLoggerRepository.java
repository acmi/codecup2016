/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.NOPLogger;

public final class NOPLoggerRepository
implements LoggerRepository {
    public boolean isDisabled(int n2) {
        return true;
    }

    public void setThreshold(Level level) {
    }

    public void emitNoAppenderWarning(Category category) {
    }

    public Level getThreshold() {
        return Level.OFF;
    }

    public Logger getLogger(String string) {
        return new NOPLogger(this, string);
    }

    public Logger getLogger(String string, LoggerFactory loggerFactory) {
        return new NOPLogger(this, string);
    }

    public Logger getRootLogger() {
        return new NOPLogger(this, "root");
    }

    public void fireAddAppenderEvent(Category category, Appender appender) {
    }

    public void resetConfiguration() {
    }
}

