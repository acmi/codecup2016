/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public interface LoggerRepository {
    public boolean isDisabled(int var1);

    public void setThreshold(Level var1);

    public void emitNoAppenderWarning(Category var1);

    public Level getThreshold();

    public Logger getLogger(String var1);

    public Logger getLogger(String var1, LoggerFactory var2);

    public Logger getRootLogger();

    public void fireAddAppenderEvent(Category var1, Appender var2);

    public void resetConfiguration();
}

