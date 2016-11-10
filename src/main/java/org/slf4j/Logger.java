/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j;

public interface Logger {
    public String getName();

    public void debug(String var1);

    public void info(String var1);

    public void warn(String var1);

    public void warn(String var1, Throwable var2);

    public void error(String var1);

    public void error(String var1, Throwable var2);
}

