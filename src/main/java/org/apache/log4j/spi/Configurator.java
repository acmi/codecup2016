/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.spi;

import java.net.URL;
import org.apache.log4j.spi.LoggerRepository;

public interface Configurator {
    public void doConfigure(URL var1, LoggerRepository var2);
}

