/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public interface XMLEventWriter {
    public void add(XMLEvent var1) throws XMLStreamException;

    public void flush() throws XMLStreamException;
}

