/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public interface PrefixResolver {
    public String getNamespaceForPrefix(String var1);

    public String getBaseIdentifier();

    public boolean handlesNullPrefixes();
}

