/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.LogManager;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.Util;
import org.slf4j.impl.Log4jLoggerAdapter;

public class Log4jLoggerFactory
implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    public Log4jLoggerFactory() {
        LogManager.getRootLogger();
    }

    public Logger getLogger(String string) {
        Logger logger = this.loggerMap.get(string);
        if (logger != null) {
            return logger;
        }
        org.apache.log4j.Logger logger2 = string.equalsIgnoreCase("ROOT") ? LogManager.getRootLogger() : LogManager.getLogger(string);
        Log4jLoggerAdapter log4jLoggerAdapter = new Log4jLoggerAdapter(logger2);
        Logger logger3 = this.loggerMap.putIfAbsent(string, log4jLoggerAdapter);
        return logger3 == null ? log4jLoggerAdapter : logger3;
    }

    static {
        try {
            Class.forName("org.apache.log4j.Log4jLoggerFactory");
            String string = "Detected both log4j-over-slf4j.jar AND bound slf4j-log4j12.jar on the class path, preempting StackOverflowError. ";
            String string2 = "See also http://www.slf4j.org/codes.html#log4jDelegationLoop for more details.";
            Util.report(string);
            Util.report(string2);
            throw new IllegalStateException(string + string2);
        }
        catch (ClassNotFoundException classNotFoundException) {
            return;
        }
    }
}

