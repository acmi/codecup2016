/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.Serializable;
import javax.xml.transform.SourceLocator;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

public class SAXSourceLocator
extends LocatorImpl
implements Serializable,
SourceLocator {
    Locator m_locator;

    public SAXSourceLocator() {
    }

    public SAXSourceLocator(Locator locator) {
        this.m_locator = locator;
        this.setColumnNumber(locator.getColumnNumber());
        this.setLineNumber(locator.getLineNumber());
        this.setPublicId(locator.getPublicId());
        this.setSystemId(locator.getSystemId());
    }

    public SAXSourceLocator(SourceLocator sourceLocator) {
        this.m_locator = null;
        this.setColumnNumber(sourceLocator.getColumnNumber());
        this.setLineNumber(sourceLocator.getLineNumber());
        this.setPublicId(sourceLocator.getPublicId());
        this.setSystemId(sourceLocator.getSystemId());
    }

    public SAXSourceLocator(SAXParseException sAXParseException) {
        this.setLineNumber(sAXParseException.getLineNumber());
        this.setColumnNumber(sAXParseException.getColumnNumber());
        this.setPublicId(sAXParseException.getPublicId());
        this.setSystemId(sAXParseException.getSystemId());
    }

    public String getPublicId() {
        return null == this.m_locator ? super.getPublicId() : this.m_locator.getPublicId();
    }

    public String getSystemId() {
        return null == this.m_locator ? super.getSystemId() : this.m_locator.getSystemId();
    }

    public int getLineNumber() {
        return null == this.m_locator ? super.getLineNumber() : this.m_locator.getLineNumber();
    }

    public int getColumnNumber() {
        return null == this.m_locator ? super.getColumnNumber() : this.m_locator.getColumnNumber();
    }
}

