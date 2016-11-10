/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm;

import org.apache.xml.dtm.DTMException;

public class DTMConfigurationException
extends DTMException {
    public DTMConfigurationException() {
        super("Configuration Error");
    }

    public DTMConfigurationException(String string) {
        super(string);
    }

    public DTMConfigurationException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

