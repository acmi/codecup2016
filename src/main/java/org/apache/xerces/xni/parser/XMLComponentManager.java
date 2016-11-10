/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.parser.XMLConfigurationException;

public interface XMLComponentManager {
    public boolean getFeature(String var1) throws XMLConfigurationException;

    public Object getProperty(String var1) throws XMLConfigurationException;
}

