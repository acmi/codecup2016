/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;

public class Logger
extends Category {
    private static final String FQCN;
    static Class class$org$apache$log4j$Logger;

    protected Logger(String string) {
        super(string);
    }

    public static Logger getLogger(Class class_) {
        return LogManager.getLogger(class_.getName());
    }

    public static Logger getRootLogger() {
        return LogManager.getRootLogger();
    }

    public boolean isTraceEnabled() {
        if (this.repository.isDisabled(5000)) {
            return false;
        }
        return Level.TRACE.isGreaterOrEqual(this.getEffectiveLevel());
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError().initCause(classNotFoundException);
        }
    }

    static {
        Class class_ = class$org$apache$log4j$Logger == null ? (Logger.class$org$apache$log4j$Logger = Logger.class$("org.apache.log4j.Logger")) : class$org$apache$log4j$Logger;
        FQCN = class_.getName();
    }
}

