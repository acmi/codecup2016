/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.SubstituteLogger;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SubstituteLoggerFactory
implements ILoggerFactory {
    boolean postInitialization = false;
    final Map<String, SubstituteLogger> loggers = new HashMap<String, SubstituteLogger>();
    final LinkedBlockingQueue<SubstituteLoggingEvent> eventQueue = new LinkedBlockingQueue();

    @Override
    public synchronized Logger getLogger(String string) {
        SubstituteLogger substituteLogger = this.loggers.get(string);
        if (substituteLogger == null) {
            substituteLogger = new SubstituteLogger(string, this.eventQueue, this.postInitialization);
            this.loggers.put(string, substituteLogger);
        }
        return substituteLogger;
    }

    public List<SubstituteLogger> getLoggers() {
        return new ArrayList<SubstituteLogger>(this.loggers.values());
    }

    public LinkedBlockingQueue<SubstituteLoggingEvent> getEventQueue() {
        return this.eventQueue;
    }

    public void postInitialization() {
        this.postInitialization = true;
    }

    public void clear() {
        this.loggers.clear();
        this.eventQueue.clear();
    }
}

