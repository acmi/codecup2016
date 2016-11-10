/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.event;

import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.SubstituteLogger;

public class SubstituteLoggingEvent
implements LoggingEvent {
    Level level;
    String loggerName;
    SubstituteLogger logger;
    String threadName;
    String message;
    Object[] argArray;
    long timeStamp;
    Throwable throwable;

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setLoggerName(String string) {
        this.loggerName = string;
    }

    public SubstituteLogger getLogger() {
        return this.logger;
    }

    public void setLogger(SubstituteLogger substituteLogger) {
        this.logger = substituteLogger;
    }

    public void setMessage(String string) {
        this.message = string;
    }

    public void setArgumentArray(Object[] arrobject) {
        this.argArray = arrobject;
    }

    public void setTimeStamp(long l2) {
        this.timeStamp = l2;
    }

    public void setThreadName(String string) {
        this.threadName = string;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}

