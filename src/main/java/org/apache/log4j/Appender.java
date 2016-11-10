/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public interface Appender {
    public void addFilter(Filter var1);

    public void close();

    public void doAppend(LoggingEvent var1);

    public String getName();

    public void setErrorHandler(ErrorHandler var1);

    public void setLayout(Layout var1);

    public void setName(String var1);

    public boolean requiresLayout();
}

