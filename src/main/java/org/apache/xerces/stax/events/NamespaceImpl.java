/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.stax.events;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.events.Namespace;
import org.apache.xerces.stax.events.AttributeImpl;

public final class NamespaceImpl
extends AttributeImpl
implements Namespace {
    private final String fPrefix;
    private final String fNamespaceURI;

    public NamespaceImpl(String string, String string2, Location location) {
        super(13, NamespaceImpl.makeAttributeQName(string), string2, null, true, location);
        this.fPrefix = string == null ? "" : string;
        this.fNamespaceURI = string2;
    }

    private static QName makeAttributeQName(String string) {
        if (string == null || string.equals("")) {
            return new QName("http://www.w3.org/2000/xmlns/", "xmlns", "");
        }
        return new QName("http://www.w3.org/2000/xmlns/", string, "xmlns");
    }

    public String getPrefix() {
        return this.fPrefix;
    }

    public String getNamespaceURI() {
        return this.fNamespaceURI;
    }

    public boolean isDefaultNamespaceDeclaration() {
        return this.fPrefix.length() == 0;
    }
}

