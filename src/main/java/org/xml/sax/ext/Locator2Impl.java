/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax.ext;

import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.LocatorImpl;

public class Locator2Impl
extends LocatorImpl
implements Locator2 {
    private String encoding;
    private String version;

    public String getXMLVersion() {
        return this.version;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setXMLVersion(String string) {
        this.version = string;
    }
}

