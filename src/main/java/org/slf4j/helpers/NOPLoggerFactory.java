/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.helpers;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;

public class NOPLoggerFactory
implements ILoggerFactory {
    public Logger getLogger(String string) {
        return NOPLogger.NOP_LOGGER;
    }
}

