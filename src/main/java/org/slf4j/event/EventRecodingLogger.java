/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.event;

import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.SubstituteLogger;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EventRecodingLogger
implements Logger {
    String name;
    SubstituteLogger logger;
    Queue<SubstituteLoggingEvent> eventQueue;

    public EventRecodingLogger(SubstituteLogger substituteLogger, Queue<SubstituteLoggingEvent> queue) {
        this.logger = substituteLogger;
        this.name = substituteLogger.getName();
        this.eventQueue = queue;
    }

    @Override
    public String getName() {
        return this.name;
    }

    private void recordEvent(Level level, String string, Object[] arrobject, Throwable throwable) {
        this.recordEvent(level, null, string, arrobject, throwable);
    }

    private void recordEvent(Level level, Marker marker, String string, Object[] arrobject, Throwable throwable) {
        SubstituteLoggingEvent substituteLoggingEvent = new SubstituteLoggingEvent();
        substituteLoggingEvent.setTimeStamp(System.currentTimeMillis());
        substituteLoggingEvent.setLevel(level);
        substituteLoggingEvent.setLogger(this.logger);
        substituteLoggingEvent.setLoggerName(this.name);
        substituteLoggingEvent.setMessage(string);
        substituteLoggingEvent.setArgumentArray(arrobject);
        substituteLoggingEvent.setThrowable(throwable);
        substituteLoggingEvent.setThreadName(Thread.currentThread().getName());
        this.eventQueue.add(substituteLoggingEvent);
    }

    @Override
    public void debug(String string) {
        this.recordEvent(Level.TRACE, string, null, null);
    }

    @Override
    public void info(String string) {
        this.recordEvent(Level.INFO, string, null, null);
    }

    @Override
    public void warn(String string) {
        this.recordEvent(Level.WARN, string, null, null);
    }

    @Override
    public void warn(String string, Throwable throwable) {
        this.recordEvent(Level.WARN, string, null, throwable);
    }

    @Override
    public void error(String string) {
        this.recordEvent(Level.ERROR, string, null, null);
    }

    @Override
    public void error(String string, Throwable throwable) {
        this.recordEvent(Level.ERROR, string, null, throwable);
    }
}

