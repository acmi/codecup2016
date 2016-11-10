/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream;

import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public interface XMLEventReader
extends Iterator {
    public boolean hasNext();

    public XMLEvent nextEvent() throws XMLStreamException;

    public XMLEvent peek() throws XMLStreamException;
}

