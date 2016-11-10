/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform.stax;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

public class StAXSource
implements Source {
    private final XMLStreamReader xmlStreamReader;
    private final XMLEventReader xmlEventReader;
    private final String systemId;

    public XMLStreamReader getXMLStreamReader() {
        return this.xmlStreamReader;
    }

    public XMLEventReader getXMLEventReader() {
        return this.xmlEventReader;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String string) {
        throw new UnsupportedOperationException("Setting systemId is not supported.");
    }
}

