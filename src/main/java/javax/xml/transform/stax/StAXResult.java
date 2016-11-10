/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform.stax;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

public class StAXResult
implements Result {
    private final XMLStreamWriter xmlStreamWriter;
    private final XMLEventWriter xmlEventWriter;

    public XMLStreamWriter getXMLStreamWriter() {
        return this.xmlStreamWriter;
    }

    public XMLEventWriter getXMLEventWriter() {
        return this.xmlEventWriter;
    }

    public String getSystemId() {
        return null;
    }
}

