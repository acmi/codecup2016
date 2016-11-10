/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.helpers;

import org.slf4j.helpers.MarkerIgnoringBase;

public class NOPLogger
extends MarkerIgnoringBase {
    public static final NOPLogger NOP_LOGGER = new NOPLogger();

    protected NOPLogger() {
    }

    public String getName() {
        return "NOP";
    }

    public final void debug(String string) {
    }

    public final void info(String string) {
    }

    public final void warn(String string) {
    }

    public final void warn(String string, Throwable throwable) {
    }

    public final void error(String string) {
    }

    public final void error(String string, Throwable throwable) {
    }
}

