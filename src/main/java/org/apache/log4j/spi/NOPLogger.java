/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.spi;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.NOPLoggerRepository;

public final class NOPLogger
extends Logger {
    public NOPLogger(NOPLoggerRepository nOPLoggerRepository, String string) {
        super(string);
        this.repository = nOPLoggerRepository;
        this.level = Level.OFF;
        this.parent = this;
    }

    public void addAppender(Appender appender) {
    }

    public void callAppenders(LoggingEvent loggingEvent) {
    }

    void closeNestedAppenders() {
    }

    public void debug(Object object) {
    }

    public void error(Object object) {
    }

    public void error(Object object, Throwable throwable) {
    }

    public void fatal(Object object, Throwable throwable) {
    }

    public Enumeration getAllAppenders() {
        return new Vector().elements();
    }

    public Level getEffectiveLevel() {
        return Level.OFF;
    }

    public void info(Object object) {
    }

    public void info(Object object, Throwable throwable) {
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public boolean isEnabledFor(Priority priority) {
        return false;
    }

    public void log(String string, Priority priority, Object object, Throwable throwable) {
    }

    public void removeAllAppenders() {
    }

    public void setLevel(Level level) {
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
    }

    public void warn(Object object) {
    }

    public void warn(Object object, Throwable throwable) {
    }

    public boolean isTraceEnabled() {
        return false;
    }
}

