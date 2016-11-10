/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import org.apache.log4j.spi.OptionHandler;

public abstract class Layout
implements OptionHandler {
    public static final String LINE_SEP = System.getProperty("line.separator");
    public static final int LINE_SEP_LEN = LINE_SEP.length();
}

