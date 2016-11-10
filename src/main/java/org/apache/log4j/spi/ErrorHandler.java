/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.OptionHandler;

public interface ErrorHandler
extends OptionHandler {
    public void setLogger(Logger var1);

    public void setBackupAppender(Appender var1);
}

